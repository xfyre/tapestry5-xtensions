package com.xfyre.tapestry5.xtensions.mixins;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
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

@Events("completionsRequested")
public class Typeahead {
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP,value="literal:<p>{{value}}</p>")
    private String template;
    
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP,value="literal:value")
    private String keys;
    
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
                "url",          resources.createEventLink ( "typeahead" ).toAbsoluteURI (),
                "displayKey",   iterableKeys[0].trim (),
                "template",     template
        );
        
        jsSupport.require ( "t5xtensions/typeaheadmixin" ).priority ( InitializationPriority.LATE ).with ( params );
    }
    
    Object onTypeahead ( @RequestParameter("t:input") String input ) throws ClassNotFoundException {
        final Holder<List<?>> holder = new Holder<List<?>> ();
        ComponentEventCallback<List<?>> callback = new ComponentEventCallback<List<?>> () {
            public boolean handleResult ( List<?> result ) {
                holder.put ( result );
                return true;
            }
        };
        
        resources.triggerEvent ( "completionsRequested", new Object[] { input }, callback );
        
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
                
                if ( suggestion.keys ().size () == 0 )
                    throw new IllegalArgumentException ( String.format ( "map %s doesn't have any of provided keys %s", map, keys ) );
            } else {
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
