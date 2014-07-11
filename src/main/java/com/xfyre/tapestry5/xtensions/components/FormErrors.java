package com.xfyre.tapestry5.xtensions.components;

import java.util.List;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.corelib.components.Errors;

/**
 * Drop-in replacement for {@link Errors} component. Displays each error in a separate alert box.
 */
public class FormErrors {
    @Environmental(false)
    private ValidationTracker tracker;

    void beginRender ( MarkupWriter writer ) {
        if ( tracker == null )
            throw new RuntimeException ( "must be enclosed in form" );

        if ( !tracker.getHasErrors () )
            return;

        /**
         * <div class="alert">
         *   <button type="button" class="close" data-dismiss="alert">&times;</button>
         *   <strong>Warning!</strong> Best check yo self, you're not looking too good.
         * </div>
         * 
         */
        
        List<String> errors = tracker.getErrors ();

        if ( !errors.isEmpty () ) {
            for ( String message : errors ) {
                writer.element ( "div", "class", "alert alert-danger alert-dismissable" );
                
                writer.element ( "button", "class", "close", "data-dismiss", "alert", "aria-hidden", "true" );
                writer.writeRaw ( "&times;" );
                writer.end (); // button
                
                writer.writeRaw ( message );
                writer.end (); // div
            }
        }
    }

}
