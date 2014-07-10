package com.xfyre.tapestry5.xtensions.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class OnEnter {
    @Parameter(required=true,defaultPrefix=BindingConstants.LITERAL)
    private String submitElement;
    
    @Inject
    private JavaScriptSupport javaScriptSupport;
    
    @InjectContainer
    private Form form;
    
    void afterRender () {
        JSONObject params = new JSONObject ();
        params.put ( "formId",   form.getClientId () );
        params.put ( "submitId", submitElement );
        javaScriptSupport.require ( "t5xtensions/onenter" ).with ( params );
    }
}
