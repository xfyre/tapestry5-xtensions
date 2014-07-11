package com.xfyre.tapestry5.xtensions.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Submit;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Bootstrap segmented radio control support
 * @author xfire
 *
 */
public class SegmentedControl implements Field {
    /**
     * Value to update
     */
    @Parameter(required=true,defaultPrefix=BindingConstants.PROP)
    private Object value;
    
    /**
     * Selection model
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.PROP)
    private SelectModel model;
    
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String clientId;
    
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String label;
    
    @Property
    private OptionModel option;
    
    @Inject
    private ComponentResources resources;
    
    @InjectComponent
    private Submit hiddenSubmit;

    @Property
    private ValueEncoder<OptionModel> optionModelEncoder = new ValueEncoder<OptionModel> () {
        public String toClient ( OptionModel value ) {
            return value.getValue () + "::" + value.getLabel ();
        }

        public OptionModel toValue ( String clientValue ) {
            String[] parts = clientValue.split ( "::" );
            return new OptionModelImpl ( parts[1], parts[0] );
        }
    };
    
    
    public Object getValue () {
        return value;
    }
    
    public void setValue ( Object value ) {
        this.value = value;
    }
    
    public SelectModel getModel () {
        return model;
    }

    public String getClientId () {
        return clientId;
    }

    public String getControlName () {
        return null;
    }

    public String getLabel () {
        return label;
    }

    public boolean isDisabled () {
        return false;
    }

    public boolean isRequired () {
        return false;
    }
    
    public String getOptionSegmentClass () {
        if ( option.getValue () != null && option.getValue ().equals ( value ) )
            return "active";
        
        return "";
    }
    
    public String getHiddenSubmitId () {
        return hiddenSubmit.getClientId ();
    }
    
}
