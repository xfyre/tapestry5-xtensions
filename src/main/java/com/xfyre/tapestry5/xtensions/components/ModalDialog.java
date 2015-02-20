package com.xfyre.tapestry5.xtensions.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Bootstrap modal dialog support
 * @author xfire
 *
 */
@Events(ModalDialog.EVENT_MODAL_HIDDEN)
public class ModalDialog implements ClientElement {
    public static final String EVENT_MODAL_HIDDEN = "modalHidden";

    @Parameter(value="prop:componentResources.id",defaultPrefix=BindingConstants.LITERAL)
    private String clientId;

    /**
     * Content block for this modal
     */
    @Property @Parameter(required=true,defaultPrefix=BindingConstants.BLOCK)
    private Block content;

    /**
     * Modal dialog title
     */
    @Property @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL)
    private String title;

    @Property @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="800")
    private Integer width;

    @Property @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="600")
    private Integer height;

    @Property @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="false")
    private Boolean enableFooter;

    /**
     * Zone to update when user dismisses modal
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP)
    private String updateZone;

    @Parameter(required=false,defaultPrefix=BindingConstants.PROP)
    private Object[] updateContext;

    @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="false")
    private Boolean hideAfterZoneUpdate;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ComponentResources resources;

    @Inject
    private Request request;

    @Override
    public String getClientId () {
        return clientId;
    }

    @AfterRender
    private void afterRender () {
        JSONObject params = new JSONObject (
                "id", getClientId (),
                "width", width,
                "height", height,
                "hide", hideAfterZoneUpdate
        );

        if ( updateZone != null ) {
            Link link = updateContext == null ?
                    resources.createEventLink ( "modalHidden" ) :
                    resources.createEventLink ( "modalHidden", updateContext );
            params.put ( "zoneId", updateZone );
            params.put ( "updateZoneLink", link.toAbsoluteURI () );
        }

        javaScriptSupport.require ( "t5xtensions/modaldialog" ).with ( params );
    }

    public Integer getLeftMargin () {
        return - ( width / 2 );
    }

}
