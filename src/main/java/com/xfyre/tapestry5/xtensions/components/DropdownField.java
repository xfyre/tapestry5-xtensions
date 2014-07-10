package com.xfyre.tapestry5.xtensions.components;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Any;
import org.apache.tapestry5.corelib.components.Hidden;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;

public class DropdownField {
    @Parameter(required=true,defaultPrefix=BindingConstants.PROP)
    private Object value;
    
    @Parameter(required=true,defaultPrefix=BindingConstants.PROP)
    private SelectModel model;
    
    @Property
    private OptionModel option;
    
    @Inject
    private ComponentResources resources;
    
    @InjectComponent
    private Any currentValueLabel, dropdownMenu, dropdownButton;
    
    @InjectComponent
    private Hidden valueField;
    
    @Environmental
    private JavaScriptSupport jsSupport;
    
    @SuppressWarnings("unchecked")
    SelectModel defaultModel()
    {
        @SuppressWarnings("rawtypes")
        Class valueType = resources.getBoundType ( "value" );

        if (valueType == null)
            return null;

        if ( Enum.class.isAssignableFrom ( valueType ) )
            return new EnumSelectModel ( valueType, resources.getContainerMessages () );

        return null;
    }
    
    void afterRender () {
        JSONObject spec = new JSONObject ( 
            "menuId",   dropdownMenu.getClientId (), 
            "labelId",  currentValueLabel.getClientId (),
            "hiddenId", valueField.getClientId (),
            "buttonId", dropdownButton.getClientId ()
        );
        
        jsSupport.require ( "t5xtensions/dropdownfield" ).with ( spec );
    }
    
    public ValueEncoder<OptionModel> getDropdownOptionEncoder () {
        return new ValueEncoder<OptionModel> () {
            public String toClient ( OptionModel value ) {
                return String.format ( "%s|%s", value.getValue (), value.getLabel () );
            }

            public OptionModel toValue ( String clientValue ) {
                String[] arr = clientValue.split ( "|" );
                return new OptionModelImpl ( arr[1], arr[0] );
            }
        };
    }
    
    public SelectModel getModel () {
        return model;
    }
    
    public String getLabel () {
        final Object currentValue = getValue ();
        
        OptionModel optionModel = (OptionModel) CollectionUtils.find ( model.getOptions (), new Predicate () {
            public boolean evaluate ( Object object ) {
                OptionModel om = (OptionModel) object;
                return currentValue.equals ( om.getValue () );
            }
        } );
        
        return optionModel.getLabel ();
    }
    
    public Object getValue () {
        if ( this.value != null )
            return this.value;
        
        return getModel ().getOptions ().get ( 0 ).getValue ();
    }
    
    public void setValue ( Object value ) {
        this.value = value;
    }
}
