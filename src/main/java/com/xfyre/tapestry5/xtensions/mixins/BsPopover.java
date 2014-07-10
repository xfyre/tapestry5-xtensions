package com.xfyre.tapestry5.xtensions.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class BsPopover {
    @InjectContainer
    private ClientElement ownerElement;
    
    @Environmental
    private JavaScriptSupport jsSupport;
    
    void afterRender ( MarkupWriter writer ) {
        jsSupport.require ( "t5xtensions/bspopover" ).with ( ownerElement.getClientId () );
    }

}
