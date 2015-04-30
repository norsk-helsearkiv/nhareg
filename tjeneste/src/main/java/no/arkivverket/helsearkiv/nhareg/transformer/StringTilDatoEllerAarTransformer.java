package no.arkivverket.helsearkiv.nhareg.transformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;
import org.apache.commons.collections4.Transformer;

/**
 *
 * @author arnfinns
 */
public class StringTilDatoEllerAarTransformer implements Transformer<String, DatoEllerAar> {

    public DatoEllerAar transform(String tid) {
        if (tid == null || tid.toLowerCase().equals("mors")) {
            return null;
        }

        DatoEllerAar dea = new DatoEllerAar();
        if (tid.length() == 4) {
            dea.setAar(Integer.parseInt(tid));
            return dea;
        }

        Calendar cal = Calendar.getInstance();
        Date dato = GyldigeDatoformater.getDate(tid);
        if (dato == null) {
            throw new IllegalArgumentException(tid);
        }
        cal.setTime(dato);
        dea.setDato(cal);
        return dea;

    }

}
