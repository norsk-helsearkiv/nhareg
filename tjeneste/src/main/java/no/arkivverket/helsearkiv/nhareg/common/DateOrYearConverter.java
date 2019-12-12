package no.arkivverket.helsearkiv.nhareg.transformer;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;
import no.arkivverket.helsearkiv.nhareg.util.PersonnummerValiderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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