package com.xfyre.tapestry5.xtensions.mixins;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.corelib.components.Form;

@MixinAfter
public class ModalIntegration {
    @InjectContainer
    private Form form;

    void afterRender ( MarkupWriter markupWriter ) {
        if ( form.getHasErrors () )
            markupWriter.attributeNS ( null, "data-submission-failed", "true" );
    }
}
