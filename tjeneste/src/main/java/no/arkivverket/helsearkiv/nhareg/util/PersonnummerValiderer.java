package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by haraldk on 27.03.15.
 */
public class PersonnummerValiderer {
    /** Lengden på gyldig organisasjonsnummer. */
    public static final int LEN_ORGNR = 9;

    /** Lengden på gyldig fødselsnummer. */
    public static final int LEN_FNR = 11;

    public static Valideringsfeil valider(String fnr) {
        if (fnr != null && !fnr.isEmpty()) {
            if (gyldigFnr(fnr)) {
                return null;
            }

            return new Valideringsfeil("fodselsnummer", "FeilFodselsnummer");
        }

        return null;
    }

    public static Valideringsfeil valider(PersondataDTO dto) {
        String fnr = dto.getFodselsnummer();
        return valider(fnr);
    }

    public static boolean isDnummer(final String nr) {
        if (nr == null || nr.isEmpty()) {
            return false;
        }
        
        String first = nr.substring(0, 1);
        int i = Integer.parseInt(first);

        return i > 3;
    }

    public static boolean isHnummer(final String nr) {
        if (nr == null || nr.isEmpty()) {
            return false;
        }
        
        String mnd = nr.substring(2, 3);
        int i = Integer.parseInt(mnd);

        return i > 3;
    }

    public static boolean isFnummer(final String nr) {
        if (nr == null || nr.isEmpty()) {
            return false;
        }
        
        return !isDnummer(nr) && !isHnummer(nr);
    }
    
    /**
     * Sjekker om fødselsnummeret har riktig format.
     *
     * @param  fodselsnummer
     * @return <code>true</code> hvis gyldig format
     */
    public static boolean gyldigFnr(final String fodselsnummer) {
        if (fodselsnummer == null || (fodselsnummer.trim().length() != LEN_FNR)) {
            return false;
        }
        
        final String fnr = fodselsnummer.trim();
        int index = 0;
        int delsum1 = 0;
        int delsum2 = 0;
        final int[] faktor1= new int[] { 3, 7, 6, 1, 8, 9, 4, 5, 2 };
        final int[] faktor2= new int[] { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };
        
        while (index < faktor2.length) {
            final int intverdi = Character.digit(fnr.charAt(index), 10);
            
            if (index < faktor1.length) { // Siffer 1-8
                delsum1+= intverdi * faktor1[index];
                delsum2+= intverdi * faktor2[index];
            } else { // Siffer 10
                final int intmod1 = 11 - (delsum1 % 11);
                if ( !((intmod1 == intverdi) || (intmod1==11 && intverdi==0))) {
                    return false;
                }
                delsum2 += intmod1 * faktor2[index];
            }
            index++;
        }
        
        final int intverdi2 = Character.digit(fnr.charAt(index), 10); // Siffer 11
        final int intmod2 = 11 - (delsum2 % 11);
        
        return ((intmod2 == intverdi2) || (intmod2 == 11 && intverdi2 == 0));
    }
}
