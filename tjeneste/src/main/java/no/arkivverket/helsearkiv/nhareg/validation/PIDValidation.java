package no.arkivverket.helsearkiv.nhareg.validation;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ValidationError;

/**
 * Created by haraldk on 27.03.15.
 */
public class PIDValidation {
    
    /** Lengden på gyldig organisasjonsnummer. */
    public static final int LEN_ORGNR = 9;

    /** Lengden på gyldig fødselsnummer. */
    private static final int LEN_FNR = 11;

    public static ValidationError validate(final String fnr) {
        if (fnr != null && !fnr.isEmpty()) {
            if (validPid(fnr)) {
                return null;
            }

            return new ValidationError("fodselsnummer", "FeilFodselsnummer");
        }

        return null;
    }

    public static ValidationError validate(final PersondataDTO personalDataDTO) {
        final String fnr = personalDataDTO.getPid();
        
        return validate(fnr);
    }

    public static boolean isDnummer(final String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }
        
        final String first = number.substring(0, 1);
        int i = Integer.parseInt(first);

        return i > 3;
    }

    public static boolean isHnummer(final String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }
        
        final String month = number.substring(2, 3);
        int i = Integer.parseInt(month);

        return i > 3;
    }

    public static boolean isFnummer(final String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }
        
        return !isDnummer(number) && !isHnummer(number);
    }
    
    /**
     * Sjekker om fødselsnummeret har riktig format.
     *
     * @param  pid PersonalID number to validate
     * @return <code>true</code> hvis gyldig format
     */
    public static boolean validPid(final String pid) {
        if (pid == null || (pid.trim().length() != LEN_FNR)) {
            return false;
        }
        
        final String fnr = pid.trim();
        int index = 0;
        int delsum1 = 0;
        int delsum2 = 0;
        final int[] faktor1 = new int[] { 3, 7, 6, 1, 8, 9, 4, 5, 2 };
        final int[] faktor2 = new int[] { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };
        
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