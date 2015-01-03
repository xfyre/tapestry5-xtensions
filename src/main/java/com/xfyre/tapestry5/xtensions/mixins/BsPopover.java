package com.xfyre.tapestry5.xtensions.mixins;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Bootstrap popover component support. Allows dynamic content loading.
 * 
 * @author xfire
 *
 */
public class BsPopover {
    /**
     * If this parameter is specified, specified Tapestry event will be triggered
     * using AJAX. Make sure to provide appropriate event handler. Event handler
     * is supposed to return JSONObject("content", "your_popover_content")
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String             popoverEvent;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private Object[]           popoverContext;

    @InjectContainer
    private ClientElement      ownerElement;

    @Environmental
    private JavaScriptSupport  jsSupport;

    @Inject
    private ComponentResources resources;

    void afterRender ( MarkupWriter writer ) {
        if ( popoverEvent == null ) {
            JSONObject params = new JSONObject ( "clientId", ownerElement.getClientId () );
            jsSupport.require ( "t5xtensions/bspopover" ).with ( params );
        } else {
            Link eventLink = resources.createEventLink ( popoverEvent, popoverContext );
            JSONObject params = new JSONObject ( 
                    "clientId", ownerElement.getClientId (), 
                    "eventLink", eventLink.toAbsoluteURI () );
            jsSupport.require ( "t5xtensions/bspopover" ).with ( params );
        }
    }

}
