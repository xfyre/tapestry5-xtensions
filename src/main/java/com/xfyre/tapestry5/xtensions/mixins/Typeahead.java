package com.xfyre.tapestry5.xtensions.mixins;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Events(Typeahead.EVENT_COMPLETIONS_REQUESTED)
public class Typeahead {
    public static final String EVENT_COMPLETIONS_REQUESTED = "completionsRequested";

    @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="true")
    private Boolean showHint;

    /**
     * Suggestion template. Equals to <strong>{{value}}</strong> by default. If you specify
     * non-default template, make sure you set <strong>keys</strong> accordingly.
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP,value="literal:<p>{{value}}</p>")
    private String template;

    /**
     * Minimum length to initiate suggestion lookup
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="2",allowNull=false)
    private Integer minLength;

    /**
     * If you provide a list of beans of list of {@link Map} objects as a suggestions list,
     * you may specify keys you're interested in.
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP,value="literal:value")
    private String keys;

    /**
     * Identifier key. If suggestions list contains duplicate names, use this key to select
     * entries by identifier (assuming that such identifier does exist). Must be used in conjunction
     * with <strong>identifierField</strong>
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP)
    private String identifierKey;

    /**
     * Identifier field DOM id (usually this is a hidden field). On selection/blur this field is set to a value
     * identified by <strong>identifierKey</strong>. On selection, special JS event <em>typeaheadmixin:selected</em>
     * is triggered on input field, so you can handle successful selection.
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP)
    private String identifierField;

    /**
     * Provide additional context values to <strong>onCompletionsRequested</strong> handler. <em>Be careful
     * when using inside loops!</em> Remember that loop variables are evaluated only during rendering phase.
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP)
    private Object[] additionalContext;

    @Inject
    private ComponentResources resources;

    @InjectContainer
    private ClientElement ownerElement;

    @Environmental
    private JavaScriptSupport jsSupport;

    @Import(library={"typeahead/typeahead.bundle.js", "typeahead/handlebars-v1.3.0.js"}, stylesheet={"typeahead/typeahead-fixes.css"})
    void afterRender () {
        String[] iterableKeys = StringUtils.split ( keys, ',' );
        JSONObject params = new JSONObject (
                "id",           ownerElement.getClientId (),
                "url",          resources.createEventLink ( "typeahead", additionalContext ).toAbsoluteURI (),
                "displayKey",   iterableKeys[0].trim (),
                "minLength",    minLength,
                "template",     template,
                "showHint",     showHint
        );

        if ( identifierKey != null && identifierField != null ) {
            params.put ( "identifierKey", identifierKey );
            params.put ( "identifierField", identifierField );
        }

        jsSupport.require ( "t5xtensions/typeaheadmixin" ).priority ( InitializationPriority.LATE ).with ( params );
    }

    Object onTypeahead ( @RequestParameter("t:input") String input, Object ... additionalContext ) throws ClassNotFoundException {
        final Holder<List<?>> holder = new Holder<> ();
        ComponentEventCallback<List<?>> callback = new ComponentEventCallback<List<?>> () {
            @Override
            public boolean handleResult ( List<?> result ) {
                holder.put ( result );
                return true;
            }
        };

        if ( additionalContext != null && additionalContext.length > 0 )
            resources.triggerEvent ( EVENT_COMPLETIONS_REQUESTED, ArrayUtils.addAll ( new Object[] { input }, additionalContext ), callback );
        else
            resources.triggerEvent ( EVENT_COMPLETIONS_REQUESTED, new Object[] { input }, callback );

        JSONArray matches = new JSONArray ();
        for ( Object obj : holder.get () ) {
            JSONObject suggestion = new JSONObject ();
            if ( obj instanceof String ) {
                String value = (String) obj;
                suggestion.put ( "value", value );
            } else if ( obj instanceof Map<?, ?> ) {
                Map<?, ?> map = (Map<?, ?>) obj;
                for ( String key : StringUtils.split ( keys, ',' ) )
                    if ( map.containsKey ( key.trim () ) )
                        suggestion.put ( key.trim (), map.get ( key.trim () ) );

                if ( identifierKey != null )
                    if ( map.containsKey ( identifierKey ) )
                        suggestion.put ( identifierKey, map.get ( identifierKey ) );
                    else
                        throw new IllegalArgumentException ( String.format ( "map %s doesn't contain identifier key %s", map, identifierKey ) );

                if ( suggestion.keys ().size () == 0 )
                    throw new IllegalArgumentException ( String.format ( "map %s doesn't have any of provided keys %s", map, keys ) );
            } else {
                if ( identifierKey != null )
                    try {
                        suggestion.put ( identifierKey, BeanUtils.getProperty ( obj, identifierKey.trim () ) );
                    } catch ( Exception e ) {
                        throw new IllegalArgumentException ( String.format ( "cannot get property value of object %s for key %s: %s", obj, identifierKey, e.getMessage ()  ) );
                    }

                for ( String key : StringUtils.split ( keys, ',' ) )
                    try {
                        suggestion.put ( key.trim (), BeanUtils.getProperty ( obj, key.trim () ) );
                    } catch ( Exception e ) {
                        throw new IllegalArgumentException ( String.format ( "cannot get property value of object %s for key %s: %s", obj, key, e.getMessage ()  ) );
                    }
            }

            matches.put ( suggestion );
        }

        return new JSONObject ( "matches", matches );
    }
}
