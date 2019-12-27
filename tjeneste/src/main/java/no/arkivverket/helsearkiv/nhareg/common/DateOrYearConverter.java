package no.arkivverket.helsearkiv.nhareg.common;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateOrYearConverter {

    public static String fromDateOrYear(final DatoEllerAar dateOrYear) {
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
    
    public static DatoEllerAar toDateOrYear(final String time) {
        if (time.toLowerCase().equals("mors") ||
                time.isEmpty() ||
                time.toLowerCase().equals("m") ||
                time.toLowerCase().equals("ukjent") ||
                time.toLowerCase().equals("u")) {
            return null;
        }

        final DatoEllerAar dateOrYear = new DatoEllerAar();
        if (time.length() == 4) {
            dateOrYear.setAar(Integer.parseInt(time));
            return dateOrYear;
        }

        final Date date = GyldigeDatoformater.getDate(time);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        dateOrYear.setDato(instance);

        return dateOrYear;
    }
    
}