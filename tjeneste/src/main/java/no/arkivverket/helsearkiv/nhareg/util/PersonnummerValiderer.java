package no.arkivverket.helsearkiv.nhareg.util;

/**
 * Created by haraldk on 27.03.15.
 */
public class PersonnummerValiderer {
    /** Lengden på gyldig organisasjonsnummer. */
    public static final int LEN_ORGNR= 9;

    /** Lengden på gyldig fødselsnummer. */
    public static final int LEN_FNR= 11;

    /**
     * Sjekker om organisasjonsnummeret har gyldig format.
     *
     * @param kundenr Kundenummer
     * @return boolean <code>true</code> hvis ikke tomt og gyldig format
     */
    public static boolean gyldigOrgnr(final String kundenr) {
        if (kundenr == null
                || kundenr.trim().length() != LEN_ORGNR) {

            return false;
        }

        final String knr= kundenr.trim();
        int i= 0;
        int resultat= 0;
        final int[] vekttall= new int[] { 3, 2, 7, 6, 5, 4, 3, 2 };
        while (i < knr.length() - 1) {
            final int intverdi= Character.digit(knr.charAt(i), 16);
            resultat= resultat + intverdi * vekttall[i];
            i++;
        }
        final int testverdi= Character.digit(knr.charAt(i), 10);
        resultat= 11 - (resultat % 11);
        if ( resultat==11 ) resultat= 0;
        return resultat == testverdi;
    }

    /**
     * Sjekker om fødselsnummeret har riktig format.
     *
     * @param  Fødselsnummer
     * @return <code>true</code> hvis gyldig format
     */
    public static boolean gyldigFnr(final String fnrOrg) {
        if ( fnrOrg == null
                || fnrOrg.trim().length() != LEN_FNR) {
            return false;
        }
        final String fnr= fnrOrg.trim();
        int index= 0;
        int delsum1= 0;
        int delsum2= 0;
        final int[] faktor1= new int[] { 3, 7, 6, 1, 8, 9, 4, 5, 2 };
        final int[] faktor2= new int[] { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };
        while (index < faktor2.length) {
            final int intverdi= Character.digit(fnr.charAt(index), 10);
            if (index < faktor1.length) { // Siffer 1-8
                delsum1+= intverdi * faktor1[index];
                delsum2+= intverdi * faktor2[index];
            }
            else { // Siffer 10
                final int intmod1= 11 - (delsum1 % 11);
                if ( !((intmod1 == intverdi) || (intmod1==11 && intverdi==0))) {
                    return false;
                }
                delsum2+= intmod1 * faktor2[index];
            }
            index++;
        }
        final int intverdi2= Character.digit(fnr.charAt(index), 10); // Siffer 11
        final int intmod2= 11 - (delsum2 % 11);
        return ((intmod2 == intverdi2) || (intmod2 == 11 && intverdi2 == 0));
    }

    public static void main(String... args){
        System.out.println(gyldigFnr("63088138996"));
    }
}
