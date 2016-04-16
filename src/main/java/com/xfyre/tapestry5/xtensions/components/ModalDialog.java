package com.xfyre.tapestry5.xtensions.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.util.Holder;
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

    @Property @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="false")
    private Boolean enableFooter;

    /**
     * Zone to monitor for updates
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP)
    private String monitorZone;

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
        final JSONObject params = new JSONObject (
                "id", getClientId (),
                "hide", hideAfterZoneUpdate
        );

        if ( updateZone != null ) {
            final Link link = updateContext == null ?
                    resources.createEventLink ( "modalHiddenInternal" ) :
                    resources.createEventLink ( "modalHiddenInternal", updateContext );
            params.put ( "updateZone", updateZone );
            params.put ( "monitorZone", monitorZone );
            params.put ( "updateZoneLink", link.toAbsoluteURI () );
        }

        javaScriptSupport.require ( "t5xtensions/modaldialog" ).with ( params );
    }

    Object onModalHiddenInternal ( EventContext ctx ) {
        final Holder<Object> holder = new Holder<> ();
        final boolean eventResult = resources.triggerContextEvent ( EVENT_MODAL_HIDDEN, ctx, result -> {
            holder.put ( result );
            return true;
        } );
        return eventResult ? holder.get () : null;
    }

}
