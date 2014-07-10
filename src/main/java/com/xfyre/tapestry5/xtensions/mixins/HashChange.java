package com.xfyre.tapestry5.xtensions.mixins;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractComponentEventLink;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.internal.InternalComponentResources;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

public class HashChange {
    @Parameter(defaultPrefix=BindingConstants.LITERAL)
    private String zone;

    @Inject
    private Logger logger;
    
    @Inject
    private ComponentResources resources;
    
    @Inject
    private Request request;
    
    @Inject
    private TypeCoercer typeCoercer;
    
    @InjectContainer 
    private AbstractComponentEventLink link;
    
    @Environmental
    private JavaScriptSupport jsSupport;

    @Import(library={"hashchange/jquery.ba-hashchange.js"})
    void afterRender ( MarkupWriter writer ) {
        jsSupport.require ( "t5/core/events" ).priority ( InitializationPriority.EARLY );
        
        InternalComponentResources internalResources = 
                (InternalComponentResources) resources.getContainerResources ();
        
        String path = null;
        
        if ( link instanceof EventLink ) {
            Binding eventBinding = internalResources.getBinding ( "event" );
            Binding contextBinding = internalResources.getBinding ( "context" );
            if ( contextBinding == null )
                path = eventBinding.get ().toString ();
            else
                path = eventBinding.get ().toString () + "_" + typeCoercer.coerce ( contextBinding.get (), String.class );
        }
        
        if ( link instanceof ActionLink ) {
            Binding contextBinding = internalResources.getBinding ( "context" );
            if ( contextBinding == null )
                path = resources.getId ();
            else
                path = resources.getId () + "_" + typeCoercer.coerce ( contextBinding.get (), String.class );
        }
        
        if ( path == null )
            throw new IllegalArgumentException ( "cannot determine hash path" );
        
        if ( internalResources.getBinding ( "zone" ) != null ) {
            Binding zoneBinding = internalResources.getBinding ( "zone" );
            if ( StringUtils.isNotBlank ( zoneBinding.get ().toString () ) ) {
                JSONObject params = new JSONObject ();
                params.put ( "linkId", link.getClientId () );
                params.put ( "zoneId", zoneBinding.get ().toString () );
                params.put ( "token", path );
                params.put ( "ajax", request.isXHR () );
                jsSupport.require ( "t5xtensions/hashchange" ).priority ( InitializationPriority.LATE ).with ( params );
            }
        }
    }
}
