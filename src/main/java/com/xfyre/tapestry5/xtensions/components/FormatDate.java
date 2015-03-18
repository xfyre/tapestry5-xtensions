package com.xfyre.tapestry5.xtensions.components;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;

/**
 * Convenience component for formatting dates
 * @author xfire
 *
 */
public class FormatDate {
    /**
     * Date value
     */
    @Parameter(required=true,defaultPrefix=BindingConstants.PROP)
    private Date date;

    /**
     * Format string
     */
    @Parameter(required=false,defaultPrefix=BindingConstants.LITERAL)
    private String format;

    @Inject
    private PersistentLocale persistentLocale;

    void beginRender ( MarkupWriter writer ) {
        if ( date == null ) {
            writer.writeRaw ( "" );
        } else {
            DateFormat df = format == null ?
                    DateFormat.getDateInstance ( DateFormat.SHORT, persistentLocale.get () ) : new SimpleDateFormat ( format );
            writer.writeRaw ( df.format ( date ) );
        }
    }
}
