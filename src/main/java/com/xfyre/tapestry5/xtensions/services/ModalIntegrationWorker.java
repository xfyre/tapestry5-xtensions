package com.xfyre.tapestry5.xtensions.services;

import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

import com.xfyre.tapestry5.xtensions.mixins.ModalIntegration;

public class ModalIntegrationWorker implements ComponentClassTransformWorker2 {
    @Override
    public void transform ( PlasticClass plasticClass, TransformationSupport support, MutableComponentModel model ) {
        if ( Form.class.getName ().equals ( plasticClass.getClassName () ) )
            model.addMixinClassName ( ModalIntegration.class.getName () );
    }

}
