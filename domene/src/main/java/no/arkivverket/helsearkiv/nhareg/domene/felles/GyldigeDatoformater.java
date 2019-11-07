package no.arkivverket.helsearkiv.nhareg.domene.felles;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by haraldk on 30.04.15.
 */
public class GyldigeDatoformater {
    private static final String[] formater = {
            "d.M.yyyy", "dd.MM.yyyy","d.MM.yyyy", "dd.M.yyyy",
            "d,M,yyyy", "dd,MM,yyyy","d,MM,yyyy", "dd,M,yyyy",
            "d-M-yyyy", "dd-MM-yyyy","d-MM-yyyy", "dd-M-yyyy",
            "ddMMyyyy", "yyyy"};

    public static Date getDate(final String dato) {
        Date d;
        for (String format : formater) {
            SimpleDateFormat df = new SimpleDateFormat(format);
            ParsePosition pos = new ParsePosition(0);
            df.setLenient(false);
            d = df.parse(dato, pos);

            if (d == null) {
                continue;
            } else if ((pos.getIndex() != format.length()) ||
                    (pos.getIndex() != dato.length())) {
                d = null;
                continue;
            }
            return d;
        }

        return null;
    }

    public static Date getDateFromYear(Integer year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.YEAR, year);

        return c.getTime();
    }

    /**
     * Returnerer en dato som er rullet med days-antall dager
     *
     * negative tall fører til rulling bakover
     * positive tall fører til rulling forover
     * @param date
     * @param days
     * @return
     */
    public static Date getDateRollDay(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.roll(Calendar.DATE, days);

        return c.getTime();
    }
    /**
     * Returnerer en dato som er rullet med days-antall dager
     *
     * negative tall fører til rulling bakover
     * positive tall fører til rulling forover
     * @param date
     * @param years
     * @return
     */
    public static Date getDateRoll(Date date, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.roll(Calendar.YEAR, years);

        return c.getTime();
    }
}
