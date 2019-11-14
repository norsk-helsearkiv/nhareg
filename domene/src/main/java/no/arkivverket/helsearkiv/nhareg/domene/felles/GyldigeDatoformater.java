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
        Date date;
        for (String format : formater) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            ParsePosition parsePosition = new ParsePosition(0);
            dateFormat.setLenient(false);
            date = dateFormat.parse(dato, parsePosition);

            if (date == null) {
                continue;
            } else if ((parsePosition.getIndex() != format.length()) ||
                    (parsePosition.getIndex() != dato.length())) {
                date = null;
                continue;
            }
            return date;
        }

        return null;
    }

    public static Date getDateFromYear(Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.YEAR, year);

        return calendar.getTime();
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.roll(Calendar.DATE, days);

        return calendar.getTime();
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.roll(Calendar.YEAR, years);

        return calendar.getTime();
    }
}
