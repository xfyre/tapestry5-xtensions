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
import org.apache.tapestry5.services.javascript.ModuleConfigurationCallback;

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

    @Import(library="intl-tel-input/js/intlTelInput.js", stylesheet="intl-tel-input/css/intlTelInput.css")
    void afterRender () {
        final JSONObject params = new JSONObject ( "elementId", ownerElement.getClientId (), "utilsPath", utilsScript.toClientURL () );
        javaScriptSupport.addModuleConfigurationCallback ( new ModuleConfigurationCallback () {
            @Override
            public JSONObject configure ( JSONObject configuration ) {
                if ( configuration.has ( "shim" ) )
                    configuration.getJSONObject ( "shim" ).put ( "intlTelInputUtils", new JSONObject ( "exports", "intlTelInputUtils" ) );
                else
                    configuration.put ( "shim", new JSONObject ( "intlTelInputUtils", new JSONObject ( "exports", "intlTelInputUtils" ) ) );

                if ( configuration.has ( "paths" ) )
                    configuration.getJSONObject ( "paths" ).put ( "intlTelInputUtils", utilsScript.toClientURL ().replaceAll ( "\\.js$", "" ) );
                else
                    configuration.put ( "paths", new JSONObject ( "intlTelInputUtils", utilsScript.toClientURL ().replaceAll ( "\\.js$", "" ) ) );

                return configuration;
            }
        } );
        javaScriptSupport.require ( "t5xtensions/phoneformat" ).priority ( InitializationPriority.LATE ).with ( params );
    }
}
