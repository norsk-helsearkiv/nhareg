package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by haraldk on 07.05.15.
 */
public class SortPasientjournaler {

    public static void sort(List<Pasientjournal> pasientjournaler){
        Collections.sort(pasientjournaler, new Comparator<Pasientjournal>() {
            @Override
            public int compare(Pasientjournal o1, Pasientjournal o2) {
                if (o1.getOpprettetDato() == null && o2.getOpprettetDato() == null) {
                    return 0;
                }
                if (o1.getOpprettetDato() != null) {
                    return -1;
                }
                if (o2.getOpprettetDato() != null) {
                    return 1;
                }
                int val =  o2.getOpprettetDato().compareTo(o1.getOpprettetDato());
                return val;
            }
        });
    }
}
