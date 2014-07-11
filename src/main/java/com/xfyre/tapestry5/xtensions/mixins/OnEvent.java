package com.xfyre.tapestry5.xtensions.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * This mixin allows handling of client-side events. When <strong>submitElement</strong> is specified,
 * client-side event triggers form submit; otherwise, <strong>serverEvent</strong> is triggered.
 * @author xfire
 *
 */
@Events("autocompleteselected")
public class OnEvent {
    /**
     * Zone to update
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL)
    private String updateZone;
    
    /**
     * Submit element
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL)
    private String submitElement;
    
    /**
     * Client event to listen to
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="click")
    private String clientEvent;
    
    /**
     * Server event to trigger
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="action")
    private String serverEvent;
    
    /**
     * Reset this field when client event is triggered 
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL)
    private String resetField;
    
    @Inject
    private JavaScriptSupport javaScriptSupport;
    
    @Inject
    private ComponentResources resources;
    
    @InjectContainer
    private ClientElement ownerElement;
    
    Object onInternalAction ( @RequestParameter("t:selectedvalue") String value ) {
        CaptureResultCallback<Object> callback = new CaptureResultCallback<Object> ();
        resources.triggerEvent ( serverEvent, new Object[] { value }, callback );
        return callback.getResult ();
    }
    
    void afterRender () {
        String[] events = clientEvent.split ( "," );
        JSONObject params = new JSONObject ();
        Link eventLink = resources.createEventLink ( "InternalAction" );
        params.put ( "url", eventLink.toAbsoluteURI () );
        params.put ( "zoneId",      updateZone );
        params.put ( "submitId",    submitElement );
        params.put ( "resetField",  resetField );
        params.put ( "clientId",    ownerElement.getClientId () );
        params.put ( "clientEvent", new JSONArray ( (Object[]) events ) );
        javaScriptSupport.require ( "t5xtensions/onevent" ).with ( params );
    }
}
