package com.xfyre.tapestry5.xtensions.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.internal.util.Holder;
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

@Events(AjaxUpload.EVENT_FILE_UPLOADED) 
@SupportsInformalParameters
public class AjaxUpload extends AbstractField {
    public static final String MULTIPART_ENCTYPE = "multipart/form-data";
    public static final String EVENT_FILE_UPLOADED = "fileUploaded";
    
    @Parameter(defaultPrefix=BindingConstants.PROP,required=true,principal=true,autoconnect=true)
    private UploadedFile value;
    
    @Parameter(defaultPrefix=BindingConstants.VALIDATE)
    private FieldValidator<Object> validate;    
    
    @Parameter(defaultPrefix=BindingConstants.LITERAL,required=true,value="false")
    private Boolean multiple;
    
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
        formSupport.setEncodingType ( MULTIPART_ENCTYPE );
        writer.element ( "div", "class", "form-control-static" );
        writer.element ( "span", "class", "btn btn-primary fileinput-button" );
        writer.element ( "i", "class", "glyphicon glyphicon-upload" ); writer.end ();
        writer.writeRaw ( "&#160;" );
        writer.element ( "span" ).raw ( messages.get ( "button.upload" ) ); writer.end ();
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

        javaScriptSupport.require ( "t5xtensions/ajaxupload" ).priority ( InitializationPriority.LATE ).with ( spec );
    }
    
    Object onUpload ( String controlName ) {
        value = decoder.getFileUpload ( controlName );
        
        final Holder<JSONObject> holder = new Holder<JSONObject> ();
        ComponentEventCallback<JSONObject> callback = new ComponentEventCallback<JSONObject> () {
            public boolean handleResult ( JSONObject result ) {
                if ( result != null ) {
                    holder.put ( result );
                    return true;
                }
                
                return false;
            }
        };
        resources.triggerEvent ( EVENT_FILE_UPLOADED, new Object[] { value }, callback );
        
        JSONArray files = new JSONArray ();
        
        if ( holder.hasValue () )
            files.put ( holder.get () );
        else
            files.put ( new JSONObject (
                    "name", value.getFileName (),
                    "size", value.getSize (),
                    "thumbnailUrl", resources.createEventLink ( "preview", value.getFileName () ).toAbsoluteURI (),
                    "deleteUrl", resources.createEventLink ( "delete", value.getFileName () ).toAbsoluteURI (),
                    "deleteType", "GET"
            ) );
        
        return new JSONObject ( "files", files );
    }

    @Override
    protected void processSubmission ( String controlName ) {
        
    }
    
}
