package com.xfyre.tapestry5.xtensions.mixins;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * This mixin adds attribute to an element if certain condition is met
 * @author xfire
 *
 */
public class AddAttribute {
    /**
     * Attribute name
     */
    @Parameter(required=true,defaultPrefix=BindingConstants.LITERAL)
    private String attrname;
    
    /**
     * Attribute value
     */
    @Parameter(required=true,defaultPrefix=BindingConstants.LITERAL)
    private String attrvalue;
    
    /**
     * Condition to check (if true, attrname is set to attrvalue)
     */
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
