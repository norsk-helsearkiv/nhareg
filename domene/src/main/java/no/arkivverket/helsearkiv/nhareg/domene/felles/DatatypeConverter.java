package no.arkivverket.helsearkiv.nhareg.domene.felles;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Skriver ut dato uten timezone.
 * @author arnfinns
 */
public class DatatypeConverter {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static String printDate(Calendar val) {
        if (val != null) {
            return format.format(val.getTime());
        } else {
            return null;
        }

    }
}
