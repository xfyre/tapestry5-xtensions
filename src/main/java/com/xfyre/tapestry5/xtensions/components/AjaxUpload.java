package com.xfyre.tapestry5.xtensions.components;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.upload.services.MultipartDecoder;
import org.apache.tapestry5.upload.services.UploadedFile;

/**
 * AJAX Upload control, based on Blueimp jQuery-File-Upload library
 * @author xfire
 *
 */
@Events(AjaxUpload.EVENT_FILE_UPLOADED) 
@SupportsInformalParameters
public class AjaxUpload extends AbstractField {
    public static final String MULTIPART_ENCTYPE = "multipart/form-data";
    public static final String EVENT_FILE_UPLOADED = "fileUploaded";
    
    /**
     * Uploaded file is bound to this parameter (required)
     */
    @Parameter(defaultPrefix=BindingConstants.PROP,required=true,principal=true,autoconnect=true)
    private UploadedFile value;
    
    /**
     * Input validator (optional)
     */
    @Parameter(defaultPrefix=BindingConstants.VALIDATE)
    private FieldValidator<Object> validate;
    
    /**
     * Additional zone to update after file upload (optional). You must provide appropriate handler
     * for <strong>fileUploaded</strong> event and return zone body from this handler.
     */
    @Parameter(defaultPrefix=BindingConstants.LITERAL)
    private String zone;
    
    /**
     * Auto-submit enclosing form on upload
     */
    @Parameter(defaultPrefix=BindingConstants.LITERAL,required=true,value="false")
    private Boolean autosubmit;
    
    @Inject
    private ComponentResources resources;
    
    @Inject
    private Messages messages;
    
    @Inject
    private MultipartDecoder decoder;
    
    @Environmental
    private JavaScriptSupport javaScriptSupport;

    public AjaxUpload () {
    }
    
    Binding defaultValidate() {
       return defaultProvider.defaultValidatorBinding ( "value", resources );
    }
    
    @SuppressWarnings("deprecation")
    AjaxUpload injectDecorator ( ValidationDecorator decorator ) {
        setDecorator ( decorator );

        return this;
    }

    AjaxUpload injectRequest ( Request request ) {
        this.request = request;

        return this;
    }

    AjaxUpload injectFormSupport ( FormSupport formSupport ) {
        // We have our copy ...
        this.formSupport = formSupport;

        // As does AbstractField
        setFormSupport ( formSupport );

        return this;
    }

    AjaxUpload injectFieldValidator ( FieldValidator<Object> validator ) {
        this.validate = validator;

        return this;
    }
    
    void beginRender ( MarkupWriter writer ) {
        String filenameId = getClientId () + "_filename";
        formSupport.setEncodingType ( MULTIPART_ENCTYPE );
        writer.element ( "div", "class", "form-control-static" );
        writer.element ( "span", "class", "fileinput-button " + cssClass );
        writer.element ( "i", "class", "glyphicon glyphicon-upload" ); writer.end ();
        writer.writeRaw ( "&#160;" );
        writer.element ( "span", "id", filenameId  ).raw ( createButtonText() ); writer.end ();
        writer.element ( "input", "type", "file", "id", getClientId (), "name", getControlName () );
        
        validate.render ( writer );
        resources.renderInformalParameters ( writer );
        decorateInsideField();
        
        writer.end (); // input field
        writer.end (); // button span
        writer.end (); // container div
    }
    
    @Import(stylesheet={"blueimp/css/jquery.fileupload.css"})
    void afterRender ( MarkupWriter writer ) {
        JSONObject spec = new JSONObject (
            "url",      resources.createEventLink ( "upload", getControlName () ).toAbsoluteURI (),
            "inputId",  getClientId ()
        );
        
        if ( StringUtils.isNotBlank ( zone ) ) {
            Link zoneUpdateUrl = resources.createEventLink ( EVENT_FILE_UPLOADED );
            
            spec.put ( "zone", zone );
            spec.put ( "zoneUpdateUrl", zoneUpdateUrl.toAbsoluteURI () );
        }
        
        javaScriptSupport.require ( "t5xtensions/ajaxupload" ).priority ( InitializationPriority.LATE ).with ( spec );
    }
    
    Object onUpload ( String controlName ) {
        value = decoder.getFileUpload ( controlName );
        
        JSONArray files = new JSONArray ();
        
        files.put ( new JSONObject (
                "name", value.getFileName (),
                "size", value.getSize (),
                "buttonText", createButtonText(),
                "thumbnailUrl", resources.createEventLink ( "preview", value.getFileName () ).toAbsoluteURI (),
                "deleteUrl", resources.createEventLink ( "delete", value.getFileName () ).toAbsoluteURI (),
                "deleteType", "GET"
        ) );
        
        return new JSONObject ( "files", files );
    }

    @Override
    protected void processSubmission ( String controlName ) {
        
    }
    
    private String createButtonText () {
    	return value == null ? messages.get ( "button.upload" ) : String.format ( "%s (%d KB)", value.getFileName (), value.getSize () / 1024 );
    }
}
