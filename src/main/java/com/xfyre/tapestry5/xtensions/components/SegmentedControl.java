package com.xfyre.tapestry5.xtensions.components;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.components.RadioGroup;
import org.apache.tapestry5.corelib.components.Submit;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Bootstrap segmented radio control support
 * @author xfire
 *
 */
public class SegmentedControl extends AbstractField {
    /**
     * Value to update
     */
    @Parameter(required=true,principal=true)
    private Object value;

    /**
     * Selection model
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP)
    private SelectModel model;

    /**
     * Validation
     */
    @Parameter(defaultPrefix = BindingConstants.VALIDATE) @Property(read=true,write=false)
    private FieldValidator<Object> validate;

    /**
     * Automatically submit enclosing form on change
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP,value="true")
    private Boolean autosubmit;

    @Parameter(name="class",required=false,defaultPrefix=BindingConstants.LITERAL)
    private String cssClass;

    @Parameter(name="buttonClass",required=false,defaultPrefix=BindingConstants.LITERAL,value="btn btn-default")
    private String buttonClass;

    @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL,value="false") @Property(read=true,write=false)
    private Boolean showCheckmark;

    @Property
    private OptionModel option;

    @Inject
    private ComponentResources resources;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @InjectComponent
    private Submit hiddenSubmit;

    @InjectComponent @Property(read=true,write=false)
    private RadioGroup segmentedControlRadio;

    @Environmental
    private ValidationTracker tracker;

    @Property
    private ValueEncoder<OptionModel> optionModelEncoder = new ValueEncoder<OptionModel> () {
        @Override
        public String toClient ( OptionModel value ) {
            return value.getValue () + "::" + value.getLabel ();
        }

        @Override
        public OptionModel toValue ( String clientValue ) {
            String[] parts = clientValue.split ( "::" );
            if ( parts.length > 1 )
                return new OptionModelImpl ( parts[1], parts[0] );
            else
                return new OptionModelImpl ( parts[0], parts[0] );
        }
    };


    void afterRender () {
        final JSONObject params = new JSONObject ( "clientId", getClientId () );
        if ( autosubmit )
            params.put ( "autosubmit", true ).put ( "submitId", hiddenSubmit.getClientId () );

        if ( showCheckmark )
            params.put ( "showCheckmark", true );

        javaScriptSupport.require ( "t5xtensions/segmentedcontrol" ).with ( params );
    }


    public Object getValue () {
        return value;
    }

    public void setValue ( Object value ) {
        if ( !disabled )
            this.value = value;
    }

    public SelectModel getModel () {
        return model;
    }

    @Override
    public boolean isRequired () {
        return false;
    }

    public String getOptionSegmentClass () {
        String segmentClass =  ( option.getValue () != null && option.getValue ().equals ( value ) ) ? buttonClass + " active" : buttonClass;
        if ( disabled ) segmentClass += " disabled";
        return segmentClass;
    }

    public String getHiddenSubmitId () {
        return hiddenSubmit.getClientId ();
    }

    public boolean isAutosubmit () {
        return autosubmit != null && autosubmit;
    }

    public String getCssClass () {
        return StringUtils.defaultString ( cssClass );
    }

    public boolean isValidationError () {
        return tracker.inError ( segmentedControlRadio );
    }

    public String getErrorText () {
        return tracker.getError ( segmentedControlRadio );
    }

    @Override
    protected void processSubmission ( String controlName ) {
    }
}
