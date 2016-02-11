package com.xfyre.tapestry5.xtensions.components;

import org.apache.commons.lang3.StringUtils;
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

    @Property @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL)
    private String width;

    @Property @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL)
    private String height;

    @Property @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="false")
    private Boolean enableFooter;

    /**
     * Zone to update when user dismisses modal
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP)
    private String updateZone;

    /**
     * Context for <b>modalHidden</b> event
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP)
    private Object[] updateContext;

    /**
     * Hide modal after updating its own zone
     */
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
                "hide", hideAfterZoneUpdate
        );

        if ( updateZone != null ) {
            Link link = updateContext == null ?
                    resources.createEventLink ( "modalHidden" ) :
                    resources.createEventLink ( "modalHidden", updateContext );
            params.put ( "zoneId", updateZone );
            params.put ( "updateZoneLink", link.toAbsoluteURI () );
        }

        if ( StringUtils.isNotBlank ( width ) )
            params.put ( "width", width );

        if ( StringUtils.isNotBlank ( height ) )
            params.put ( "height", height );

        javaScriptSupport.require ( "t5xtensions/modaldialog" ).with ( params );
    }

//    public Integer getLeftMargin () {
//        return - ( width / 2 );
//    }

}
