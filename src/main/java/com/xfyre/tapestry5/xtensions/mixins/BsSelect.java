package com.xfyre.tapestry5.xtensions.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class BsSelect {
    @InjectContainer
    private ClientElement ownerElement;

    @Environmental
    private JavaScriptSupport jsSupport;

    @Import(stylesheet="selectpicker/bootstrap-select.min.css",library="selectpicker/bootstrap-select.min.js")
    void afterRender ( MarkupWriter writer ) {
        jsSupport.require ( "t5xtensions/bsselect" ).priority ( InitializationPriority.LATE ).with ( ownerElement.getClientId () );
    }

}
