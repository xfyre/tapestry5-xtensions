package com.xfyre.tapestry5.xtensions.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Allows to toggle Bootstrap dropdown when dropdown menu link is in fact an AJAX action
 * @author xfire
 *
 */
public class ToggleDropdown {
    /**
     * Pass dropdown button id
     */
    @Parameter(required=true,defaultPrefix=BindingConstants.LITERAL)
    private String dropdownId;
    
    @InjectContainer
    private ClientElement ownerElement;
    
    @Environmental
    private JavaScriptSupport jsSupport;
    
    void afterRender ( MarkupWriter writer ) {
        jsSupport.require ( "t5xtensions/toggledropdown" ).with ( ownerElement.getClientId (), dropdownId );
    }
}
