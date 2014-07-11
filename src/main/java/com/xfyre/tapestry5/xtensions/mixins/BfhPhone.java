package com.xfyre.tapestry5.xtensions.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Integration for Bootstrap Form Helpers phone component
 * @author xfire
 *
 */
public class BfhPhone {
    @Inject
    private JavaScriptSupport javaScriptSupport;
    
    @InjectContainer
    private ClientElement ownerElement;
    
    @Import(library={"bfh/js/bootstrap-formhelpers.js"})
    void afterRender () {
        javaScriptSupport.require ( "t5xtensions/bfhphone" ).with ( ownerElement.getClientId () );
    }
}
