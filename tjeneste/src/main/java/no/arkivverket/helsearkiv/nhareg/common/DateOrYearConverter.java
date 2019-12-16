package no.arkivverket.helsearkiv.nhareg.common;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;

import java.util.Calendar;
import java.util.Date;

public class DateOrYearConverter {

    public static DatoEllerAar tilDatoEllerAar(String tid) {
        if (tid.toLowerCase().equals("mors") ||
                tid.isEmpty() ||
                tid.toLowerCase().equals("m") ||
                tid.toLowerCase().equals("ukjent") ||
                tid.toLowerCase().equals("u")) {
            return null;
        }

        DatoEllerAar datoEllerAar = new DatoEllerAar();
        if (tid.length() == 4) {
            datoEllerAar.setAar(Integer.parseInt(tid));
            return datoEllerAar;
        }

        Date dato = GyldigeDatoformater.getDate(tid);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dato);
        datoEllerAar.setDato(cal);

        return datoEllerAar;
    }
}