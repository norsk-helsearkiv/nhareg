package no.arkivverket.helsearkiv.nhareg.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;
import no.arkivverket.helsearkiv.nhareg.tjeneste.KonfigparamTjeneste;
import no.arkivverket.helsearkiv.nhareg.transformer.Konverterer;

import javax.persistence.EntityManager;

/**
 * Validerer konsistens på datofeltene til PersondataDTO
 * Benyttes i POST og PUt
 * @author robing
 */
public class DatoValiderer {
    //Hjelpemetoder for validering
    public static ArrayList<Valideringsfeil> valider(PersondataDTO person, KonfigparamTjeneste konfig) throws ParseException {
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        //TODO legge på sjekker for hvilke datoer (perioder) som faktisk er lovlig å legge inn i systemet

        if(person == null) {
            return feil;
        }
        //født kan ikke være mors
        if (sjekkMorsUkjent(person.getFodt(), "mors")){
            feil.add(new Valideringsfeil("fodt", "DagEllerAar"));
        }
        //død kan ikke være ukjent
        if (sjekkMorsUkjent(person.getDod(), "ukjent")) {
            feil.add(new Valideringsfeil("dod", "DagEllerAar"));
        }

        if (feil.size()>0){
            return feil;
        }
        Date lowLim = konfig.getDate(KonfigparamTjeneste.KONFIG_LOWLIM);
        Integer waitLim = konfig.getInt(KonfigparamTjeneste.KONFIG_WAITLIM);
        Date maxLim = GyldigeDatoformater.getDateRoll(new Date(), -waitLim);
        Integer maxAge = konfig.getInt(KonfigparamTjeneste.KONFIG_MAXAGE);
        Date minLim = GyldigeDatoformater.getDateRoll(new Date(), -maxAge);

        //skjema 1
        List<Valideringsfeil> fnumfeil = fnumSjekk(person, lowLim, maxLim);
        leggTilFeil(feil, fnumfeil);
        //skjema 2a
        if ("ukjent".equals(person.getFodt()) ) {//mors sjekkes i metoden pga feilmelding hvis den mangler
            List<Valideringsfeil> pdatofeil = pasientjournaldatoerutenkjentfogmors(person, lowLim, maxLim);
            leggTilFeil(feil, pdatofeil);
        }
        //skjema 2b
        if ("mors".equals(person.getDod()) && sjekk(person.getFodt())) {
            List<Valideringsfeil> pdatofeilb = pasientjournaldatokjentfdatoukjentdod(person, minLim);
            leggTilFeil(feil, pdatofeilb);
        }
        //skjema 2c
        if ("ukjent".equals(person.getFodt()) && sjekk(person.getDod())) {
            List<Valideringsfeil> pdatofeilc = pasientjournaldatokjentmorsukjentfodt(person, lowLim, maxLim);
            leggTilFeil(feil, pdatofeilc);
        }
        //skjema 2d
        if (sjekk(person.getFodt())&&sjekk(person.getDod())) {
            List<Valideringsfeil> pdatofeild = pasientjournaldatokjentfodtogmors(person, lowLim, maxLim);
            leggTilFeil(feil, pdatofeild);
        }
        /*

        //Regler basert på født
        if(sjekk(person.getFodt())) {

            Date fodt = getDate(person.getFodt());


            if (fodt.before(lowLim)||fodt.after(maxLim)){
                feil.add(new Valideringsfeil("fodt", "UtenforGyldigPeriode"));
            }


            if(sjekk(person.getDod())) {
                Date dod = getDate(person.getDod());
                if(fodt.after(dod)) {
                    feil.add(new Valideringsfeil("fodt", "FodtEtterDodt"));
                }
            }
            
            if(sjekk(person.getfKontakt())) {
                Date fKontakt = getDate(person.getfKontakt());
                if(fodt.after(fKontakt)) {
                    feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
                }
            }
            
            if(sjekk(person.getsKontakt())) {
                Date sKontakt = getDate(person.getsKontakt());
                if(fodt.after(sKontakt)) {
                    feil.add(new Valideringsfeil("sKontakt", "sKontaktForFodt"));
                }
            }
        }
        */
        //Regler basert på død (ikke mors)
       /* if(sjekk(person.getDod())) {
            Date dod = getDate(person.getDod());
            if (dod.before(lowLim)||dod.after(maxLim)){
                feil.add(new Valideringsfeil("dod", "UtenforGyldigPeriode"));
            }
            //første og siste kontaktdato registrert
            if (sjekk(person.getfKontakt())&&sjekk(person.getsKontakt())){
                Date fKontakt = getDate(person.getfKontakt());
                Date sKontakt = getDate(person.getsKontakt());
                if(fKontakt.after(sKontakt)) {
                    feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
                }
                if(sKontakt.after(dod)) {
                    feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
                }
            }
            //bare siste kontaktdato registrert
            else if (sjekk(person.getsKontakt())){
                Date sKontakt = getDate(person.getsKontakt());
                if(sKontakt.after(dod)) {
                    feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
                }
            }
            //bare første kontaktdato registrert
            else if (sjekk(person.getfKontakt())){
                Date fKontakt = getDate(person.getfKontakt());
                if (fKontakt.after(dod)){
                    feil.add(new Valideringsfeil("fKontakt", "fKontaktEtterDod"));
                }
            }
        }else{ //MORS
            if (sjekk(person.getsKontakt())){

            }
            if(sjekk(person.getfKontakt()) && sjekk(person.getsKontakt())) {
                Date fKontakt = getDate(person.getfKontakt());
                Date sKontakt = getDate(person.getsKontakt());

                if(fKontakt.after(sKontakt)) {
                    feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
                }
            }

        }*/


        return feil;
    }
    private static void leggTilFeil(List<Valideringsfeil> feilliste, List<Valideringsfeil> nyeFeil){
        if (!nyeFeil.isEmpty()){
            feilliste.addAll(nyeFeil);
        }
    }

    //skjema 01
    private static ArrayList<Valideringsfeil> fnumSjekk(PersondataDTO person, Date lowLim, Date maxLim) {
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        String fnr = person.getFodselsnummer();
        if ("ukjent".equals(person.getFodt())){
            //kjønn er validert i input.
        }else{
            if (fnr==null||"".equals(fnr)){
               //sjekk fødselsdato

                if (sjekk(person.getFodt())){
                    Date fodt = getDate(person.getFodt());
                    if (fodt.before(lowLim)||fodt.after(maxLim)){
                        feil.add(new Valideringsfeil("fodt", "UtenforGyldigPeriode"));
                    }
                }
            }
            else {
                Valideringsfeil fnrfeil = PersonnummerValiderer.valider(person);
                if (fnrfeil != null) {
                    feil.add(fnrfeil);
                }
            }
        }
        return feil;
    }

    //skjema 02a
    private static ArrayList<Valideringsfeil> pasientjournaldatoerutenkjentfogmors(PersondataDTO person, Date lowLim, Date maxLim){
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();

        //TODO skal kanskje ikke være her iflg NYTT skjema..
        /*
        if(sjekk(person.getDod())) {
            Date dod = getDate(person.getDod());
            if (dod.before(lowLim)||dod.after(maxLim)){
                feil.add(new Valideringsfeil("dod", "UtenforGyldigPeriode"));
            } else {
                //første og siste kontaktdato registrert
                if (sjekk(person.getfKontakt()) && sjekk(person.getsKontakt())) {
                    Date fKontakt = getDate(person.getfKontakt());
                    Date sKontakt = getDate(person.getsKontakt());
                    if (fKontakt.after(sKontakt)) {
                        feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
                    }
                    if (sKontakt.after(dod)) {
                        feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
                    }
                }
                //bare siste kontaktdato registrert
                else if (sjekk(person.getsKontakt())) {
                    Date sKontakt = getDate(person.getsKontakt());
                    if (sKontakt.after(dod)) {
                        feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
                    }
                }
                //bare første kontaktdato registrert
                else if (sjekk(person.getfKontakt())) {
                    Date fKontakt = getDate(person.getfKontakt());
                    if (fKontakt.after(dod)) {
                        feil.add(new Valideringsfeil("fKontakt", "fKontaktEtterDod"));
                    }
                }
            }
        }
        else */if ("mors".equals(person.getDod())){

            if (sjekk(person.getsKontakt()) && sjekk(person.getfKontakt())){
                Date sKontakt = getDate(person.getsKontakt());
                Date fKontakt = getDate(person.getfKontakt());

                if (fKontakt.after(sKontakt)) {
                    feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
                }

                if (sKontakt.before(lowLim)||sKontakt.after(maxLim)){
                    feil.add(new Valideringsfeil("sKontakt", "UtenforGyldigPeriode"));
                }
            }
        }else{
            feil.add(new Valideringsfeil("dod", "manglermors"));
        }

        return feil;
    }
    //skjema 02b
    private static List<Valideringsfeil> pasientjournaldatokjentfdatoukjentdod(PersondataDTO person, Date maxAge) {
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        if (sjekk(person.getDod())||"mors".equals(person.getDod())){
            List<Valideringsfeil> f = sjekkKontaktdatoFodt(person);
            if (!f.isEmpty()){
                feil.addAll(f);
            }
        }else{
            if (sjekk(person.getFodt())){
                Date fodt = getDate(person.getFodt());
                if (fodt.before(maxAge)){
                    feil.add(new Valideringsfeil("fodt", "UtenforGyldigPeriode"));
                }else{
                    List<Valideringsfeil> f = sjekkKontaktdatoFodt(person);
                    if (!f.isEmpty()) {
                        feil.addAll(f);
                    }
                }


            }else{
                //
                //TODO. født mangler, håndteres ved kalling av funksjo.. mao vi kommer ikke hit..
            }
        }
        return feil;
    }

    //skjema 02c
    //TODO kaller må påse at født er angitt som "ukjent" og at morsdato/år er kjent
    private static List<Valideringsfeil> pasientjournaldatokjentmorsukjentfodt(PersondataDTO person, Date lowLim, Date maxLim){
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        Date dod = getDate(person.getDod());
        if (dod.before(lowLim)||dod.after(maxLim)){
            feil.add(new Valideringsfeil("dod", "UtenforGyldigPeriode"));
        } else {
            List<Valideringsfeil> f = sjekkKontaktdatoDod(person);
            if (!f.isEmpty()){
                feil.addAll(f);
            }
        }

        return feil;
    }

    //skjema 02d
    private static List<Valideringsfeil> pasientjournaldatokjentfodtogmors(PersondataDTO person, Date lowLim, Date maxLim){
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        Date dod = getDate(person.getDod());
        if (dod.before(lowLim)||dod.after(maxLim)){
            feil.add(new Valideringsfeil("dod", "UtenforGyldigPeriode"));
        } else {
            if (sjekk(person.getFodt())&&sjekk(person.getDod())){
                Date fodt = getDate(person.getFodt());
                if (dod.before(fodt)){
                    feil.add(new Valideringsfeil("fodt", "FodtEtterDodt"));
                }else{
                    List<Valideringsfeil> f = sjekkKontaktdatoFodtogDod(person);
                    if (!f.isEmpty()){
                        feil.addAll(f);
                    }
                }

            }
        }
        return feil;
    }

    private static List<Valideringsfeil> sjekkKontaktdatoFodtogDod(PersondataDTO person){
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        Date fodt = getDate(person.getFodt());
        Date dod = getDate(person.getDod());

        //første og siste kontaktdato registrert
        if (sjekk(person.getfKontakt()) && sjekk(person.getsKontakt())) {
            Date fKontakt = getDate(person.getfKontakt());
            Date sKontakt = getDate(person.getsKontakt());
            if (fKontakt.after(sKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }
            //første kan ikke være før født
            if(fodt.after(fKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }
            if (sKontakt.after(dod)) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }
        }
        //bare siste kontaktdato registrert
        else if (sjekk(person.getsKontakt())) {
            Date sKontakt = getDate(person.getsKontakt());

            if(fodt.after(sKontakt)) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktForFodt"));
            }
            if (sKontakt.after(dod)) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }
        }
        //bare første kontaktdato registrert
        else if (sjekk(person.getfKontakt())) {
            Date fKontakt = getDate(person.getfKontakt());
            //første kan ikke være før født
            if(fodt.after(fKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }
            if (fKontakt.after(dod)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEtterDod"));
            }
        }
        return feil;
    }
    /**
     * Sjekker kontaktdatoer basert på født
     * @param person
     * @return
     */
    private static List<Valideringsfeil> sjekkKontaktdatoFodt(PersondataDTO person){
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        Date fodt = getDate(person.getFodt());
        //første og siste kontaktdato registrert
        if (sjekk(person.getfKontakt()) && sjekk(person.getsKontakt())) {
            Date fKontakt = getDate(person.getfKontakt());
            Date sKontakt = getDate(person.getsKontakt());

            if (fKontakt.after(sKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }

            //første kan ikke være før født
            if(fodt.after(fKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }
        }
        //bare siste kontaktdato registrert
        else if (sjekk(person.getsKontakt())) {
            Date sKontakt = getDate(person.getsKontakt());
            if(fodt.after(sKontakt)) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktForFodt"));
            }

        }
        //bare første kontaktdato registrert
        else if (sjekk(person.getfKontakt())) {
            Date fKontakt = getDate(person.getfKontakt());
            //første kan ikke være før født
            if(fodt.after(fKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }
        }
        return feil;
    }

    /**
     * Sjekker kontaktdatoer basert på død
     * @param person
     * @return
     */
    private static List<Valideringsfeil> sjekkKontaktdatoDod(PersondataDTO person){
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        Date dod = getDate(person.getDod());
        //første og siste kontaktdato registrert
        if (sjekk(person.getfKontakt()) && sjekk(person.getsKontakt())) {
            Date fKontakt = getDate(person.getfKontakt());
            Date sKontakt = getDate(person.getsKontakt());

            if (fKontakt.after(sKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }

            //siste kan ikke være etter død
            if(sKontakt.after(dod)) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }
        }
        //bare siste kontaktdato registrert
        else if (sjekk(person.getsKontakt())) {
            Date sKontakt = getDate(person.getsKontakt());
            if(sKontakt.after(dod)) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }

        }
        //bare første kontaktdato registrert
        else if (sjekk(person.getfKontakt())) {
            Date fKontakt = getDate(person.getfKontakt());
            //første kan ikke være etter død
            if (fKontakt.after(dod)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEtterDod"));
            }
        }
        return feil;
    }

    public static Date getDate(String dato){
        return GyldigeDatoformater.getDate(dato);
    }

    
    private static boolean sjekk(String s) {
        if(s == null || s.isEmpty() || s.toLowerCase().equals("mors") || s.toLowerCase().equals("ukjent")) {
            return false;
        }
        
        return getDate(s) != null;
    }

    private static boolean sjekkMorsUkjent(String s, String toCheck){
        if(s == null || s.isEmpty()) {
            return false;
        }
        return (s.toLowerCase().equals(toCheck));
    }

}
