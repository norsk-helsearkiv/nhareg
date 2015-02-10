package no.arkivverket.helsearkiv.nhareg.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            cal.setTime(sdf.parse(tid));
        } catch (ParseException ex) {
            throw new IllegalArgumentException(tid);
        }

        dea.setDato(cal);
        return dea;

    }

}
