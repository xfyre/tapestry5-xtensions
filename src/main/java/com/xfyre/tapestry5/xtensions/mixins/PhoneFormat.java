package com.xfyre.tapestry5.xtensions.mixins;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Integration for Bootstrap Form Helpers phone component
 * @author xfire
 *
 */
public class PhoneFormat {
    @Inject
    private JavaScriptSupport javaScriptSupport;

    @InjectContainer
    private ClientElement ownerElement;

    @Inject @Path("intl-tel-input/js/utils.js")
    private Asset utilsScript;

    @Import(library="intl-tel-input/js/intlTelInput.min.js", stylesheet="intl-tel-input/css/intlTelInput.css")
    void afterRender () {
        final JSONObject params = new JSONObject ( "elementId", ownerElement.getClientId (), "utilsPath", utilsScript.toClientURL () );
        javaScriptSupport.require ( "t5xtensions/phoneformat" ).priority ( InitializationPriority.LATE ).with ( params );
    }
}
