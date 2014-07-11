package com.xfyre.tapestry5.xtensions.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Customizable confirmation dialog for Bootstrap
 * @author xfire
 *
 */
public class BsConfirm {
    /**
     * Confirmation title
     */
    @Parameter(required=false,value="message:title.confirm",defaultPrefix=BindingConstants.LITERAL)
    private String title;
    
    /**
     * Confirmation message
     */
    @Parameter(required=false,value="message:confirm.action",defaultPrefix=BindingConstants.LITERAL)
    private String message;
    
    /**
     * Confirm button text
     */
    @Parameter(required=false,value="message:button.ok",defaultPrefix=BindingConstants.LITERAL)
    private String confirm;
    
    /**
     * Confirm button CSS class
     */
    @Parameter(required=false,value="btn-primary",defaultPrefix=BindingConstants.LITERAL)
    private String confirmClass;
    
    /**
     * Cancel button text 
     */
    @Parameter(required=false,value="message:button.cancel",defaultPrefix=BindingConstants.LITERAL)
    private String cancel;
    
    @Inject
    private Environment environment;

    @Environmental
    private JavaScriptSupport jsSupport;
    
    @InjectContainer
    private ClientElement ownerElement;
    
    void afterRender ( MarkupWriter writer ) {
        JSONObject spec = new JSONObject ();
        spec.put ( "clientId", ownerElement.getClientId () );
        spec.put ( "title", title );
        spec.put ( "messagehash", message.hashCode () );
        spec.put ( "message", message );
        spec.put ( "confirm", confirm );
        spec.put ( "confirmClass", confirmClass );
        spec.put ( "cancel", cancel );
        
        FormSupport formSupport = environment.peek ( FormSupport.class );
        if ( formSupport != null )
            spec.put ( "formId", formSupport.getClientId () );
        
        // EARLY initialization is required to bypass Tapestry.waitForPage()
        jsSupport.require ( "t5xtensions/bsconfirm" ).priority ( InitializationPriority.EARLY ).with ( spec );
    }
    
}
