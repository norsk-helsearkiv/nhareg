package no.arkivverket.helsearkiv.nhareg.domene.felles;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by haraldk on 30.04.15.
 */
public class GyldigeDatoformater {
    public static Date getDate(String dato) {
        String[] formater = {"dd.MM.yyyy", "dd,MM,yyyy", "dd-MM-yyyy", "ddMMyyyy", "yyyy"};
        Date d = null;
        for (String format : formater) {

            SimpleDateFormat df = new SimpleDateFormat(format);
            ParsePosition pos = new ParsePosition(0);
            df.setLenient(false);
            d = df.parse(dato, pos);

            if (d == null) {
                continue;
            } else if (pos.getIndex() != dato.length()) {
                d = null;
                continue;
            }
            return d;
        }
        return null;
    }
}
