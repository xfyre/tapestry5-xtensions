package com.xfyre.tapestry5.xtensions.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Events("modalHidden")
public class ModalDialog implements ClientElement {
    @Parameter(value="prop:componentResources.id",defaultPrefix=BindingConstants.LITERAL)  
    private String clientId;  
     
    @Property @Parameter(required=true,defaultPrefix=BindingConstants.BLOCK)  
    private Block content;  
     
    @Property @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL)  
    private String title;  
    
    @Property @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="800")
    private Integer width;
    
    @Property @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="600")
    private Integer height;
    
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP)
    private String updateZone;
    
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP)
    private Object[] updateContext;
    
    @Inject  
    private JavaScriptSupport javaScriptSupport;
    
    @Inject
    private ComponentResources resources;
    
    @Inject
    private Request request;
    
    public String getClientId () {
        return clientId;
    }
     
    @AfterRender  
    private void afterRender () {
        JSONObject params = new JSONObject (
                "id", getClientId (),
                "width", width,
                "height", height
        );
        
        if ( updateZone != null ) {
            Link link = updateContext == null ? 
                    resources.createEventLink ( "modalHidden" ) :
                    resources.createEventLink ( "modalHidden", updateContext );
            params.put ( "zoneId", updateZone );
            params.put ( "updateZoneLink", link.toAbsoluteURI () );
        }
        
        javaScriptSupport.require ( "t5xtensions/modaldialog" ).with ( params );
    }
    
    public Integer getLeftMargin () {
        return - ( width / 2 );
    }
    
}
