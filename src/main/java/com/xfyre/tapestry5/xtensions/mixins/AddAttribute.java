package com.xfyre.tapestry5.xtensions.mixins;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class AddAttribute {
    @Parameter(required=true,defaultPrefix=BindingConstants.LITERAL)
    private String attrname;
    
    @Parameter(required=true,defaultPrefix=BindingConstants.LITERAL)
    private String attrvalue;
    
    @Parameter(required=true,defaultPrefix=BindingConstants.PROP)
    private Boolean condition;
    
    @InjectContainer
    private ClientElement ownerElement;
    
    @Inject
    private JavaScriptSupport javaScriptSupport;
    
    void afterRender ( MarkupWriter writer ) {
        if ( condition )
            javaScriptSupport.require ( "t5xtensions/addattribute" ).with ( ownerElement.getClientId (), attrname, attrvalue );
    }
}
