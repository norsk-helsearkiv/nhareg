package no.arkivverket.helsearkiv.nhareg.util;

import java.text.SimpleDateFormat;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import org.apache.commons.collections4.Transformer;

/**
 *
 * @author arnfinns
 */
public class DatoEllerAarTilStringTransformer implements Transformer<DatoEllerAar, String> {

    public String transform(DatoEllerAar dato) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        if (dato != null) {
            if (dato.getDato() != null) {
                return format.format(dato.getDato().getTime());
            } else {
                return dato.getAar().toString();
            }
        }
        return null;
    }

}
