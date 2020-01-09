package no.arkivverket.helsearkiv.nhareg.common;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DateOrYear;
import no.arkivverket.helsearkiv.nhareg.domene.felles.ValidDateFormats;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateOrYearConverter {

    public static String fromDateOrYear(final DateOrYear dateOrYear) {
        final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        
        if (dateOrYear == null) {
            return null;
        }

        if (dateOrYear.getDato() != null) {
            return format.format(dateOrYear.getDato().getTime());
        } else {
            return dateOrYear.getAar().toString();
        }
    }
    
    public static DateOrYear toDateOrYear(final String time) {
        if (time.toLowerCase().equals("mors") ||
                time.isEmpty() ||
                time.toLowerCase().equals("m") ||
                time.toLowerCase().equals("ukjent") ||
                time.toLowerCase().equals("u")) {
            return null;
        }

        final DateOrYear dateOrYear = new DateOrYear();
        if (time.length() == 4) {
            dateOrYear.setAar(Integer.parseInt(time));
            return dateOrYear;
        }

        final Date date = ValidDateFormats.getDate(time);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        dateOrYear.setDato(instance);

        return dateOrYear;
    }
    
}