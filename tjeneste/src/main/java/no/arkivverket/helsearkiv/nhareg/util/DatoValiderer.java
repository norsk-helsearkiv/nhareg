package no.arkivverket.helsearkiv.nhareg.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;
import no.arkivverket.helsearkiv.nhareg.tjeneste.KonfigparamTjeneste;
import no.arkivverket.helsearkiv.nhareg.transformer.DatoEllerAarTilStringTransformer;
import no.arkivverket.helsearkiv.nhareg.transformer.Konverterer;

import javax.persistence.EntityManager;

/**
 * Validerer konsistens på datofeltene til PersondataDTO
 * Benyttes i POST og PUt
 * @author robing
 */
public class DatoValiderer {
    /**
     * Diagnosedato/år valideres med javax.validation
     *
     * @param diagnose
     * @param pasientjournal
     * @param konfig
     * @return
     */
    public static List<Valideringsfeil> validerDiagnose(DiagnoseDTO diagnose, Pasientjournal pasientjournal, KonfigparamTjeneste konfig){
        List<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        Grunnopplysninger gr = pasientjournal.getGrunnopplysninger();

        //ta diagnosedato som god fisk ettersom både mors og født er ukjent...
        if (gr.isFodtdatoUkjent()!=null&&gr.isFodtdatoUkjent() && gr.isDødsdatoUkjent()!=null&&gr.isDødsdatoUkjent()){
            return feil;
        }

        DatoEllerAarTilStringTransformer trans = new DatoEllerAarTilStringTransformer();
        String diag = diagnose.getDiagnosedato();
        Date diagDato = getDate(diag);

        //fødtdatoår kjent
        if (gr.isFodtdatoUkjent()==null || !gr.isFodtdatoUkjent()){
            DatoEllerAar fodt = gr.getFødt();
            String fodtString  =trans.transform(fodt);

            //Date fodtDato = getDate(fodtString);

            if ((compareDateString(fodtString, diag)==DateCompareResult.AFTER)){
                feil.add(new Valideringsfeil("diagnosedato", "DiagForFodt"));
            }
            /*if (fodtDato.after(diagDato)){
                feil.add(new Valideringsfeil("diagnosedato", "DiagForFodt"));
            }*/
        }
        if (gr.isDødsdatoUkjent()==null || !gr.isDødsdatoUkjent()){
            DatoEllerAar dod = gr.getDød();
            String dodString = trans.transform(dod);
            //Date dodDato = getDate(dodString);
            if ((compareDateString(dodString, diag)==DateCompareResult.BEFORE)){
                feil.add(new Valideringsfeil("diagnosedato", "DiagEtterDod"));
            }

            /*if (dodDato.before(diagDato)){
                feil.add(new Valideringsfeil("diagnosedato", "DiagEtterDod"));
            }*/
        }
        return feil;
    }

    //Hjelpemetoder for validering
    public static ArrayList<Valideringsfeil> valider(PersondataDTO person, KonfigparamTjeneste konfig) throws ParseException {
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();

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
                    //TODO sjekke også her på år?
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
        if ("mors".equals(person.getDod())){

            if (sjekk(person.getsKontakt()) && sjekk(person.getfKontakt())){
                Date sKontakt = getDate(person.getsKontakt());
                Date fKontakt = getDate(person.getfKontakt());
                if ((compareDateString(person.getfKontakt(), person.getsKontakt()))==DateCompareResult.AFTER){
                    feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
                }
                /*if (fKontakt.after(sKontakt)) {
                    feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
                }*/

                //TODO sammenligne disse også på kun ÅR?
                if (sKontakt.before(lowLim)||sKontakt.after(maxLim)){
                    feil.add(new Valideringsfeil("sKontakt", "UtenforGyldigPeriode"));
                }
            }
        }else if (!sjekk(person.getDod()) ){
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
                //TODO også her kun på år?
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
        //TODO også her sammenlign på år?
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
        //TODO også her sammenligne på ÅR?
        if (dod.before(lowLim)||dod.after(maxLim)){
            feil.add(new Valideringsfeil("dod", "UtenforGyldigPeriode"));
        } else {
            if (sjekk(person.getFodt())&&sjekk(person.getDod())){
                Date fodt = getDate(person.getFodt());
                if (compareDateString(person.getDod(), person.getFodt())==DateCompareResult.BEFORE){
                    feil.add(new Valideringsfeil("fodt", "FodtEtterDodt"));
                }/*
                if (dod.before(fodt)){
                    feil.add(new Valideringsfeil("fodt", "FodtEtterDodt"));
                }*/else{
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
            if (compareDateString(person.getfKontakt(), person.getsKontakt())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }
            /*
            if (fKontakt.after(sKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }*/
            //første kan ikke være før født
            if (compareDateString(person.getFodt(), person.getfKontakt())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }/*
            if(fodt.after(fKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }*/
            if (compareDateString(person.getsKontakt(), person.getDod())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }
            /*
            if (sKontakt.after(dod)) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }*/
        }
        //bare siste kontaktdato registrert
        else if (sjekk(person.getsKontakt())) {
            Date sKontakt = getDate(person.getsKontakt());
            if (compareDateString(person.getFodt(), person.getsKontakt())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("sKontakt", "sKontaktForFodt"));
            }/*
            if(fodt.after(sKontakt)) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktForFodt"));
            }*/
            if (compareDateString(person.getsKontakt(), person.getDod())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }
            /*
            if (sKontakt.after(dod)) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }*/
        }
        //bare første kontaktdato registrert
        else if (sjekk(person.getfKontakt())) {
            Date fKontakt = getDate(person.getfKontakt());
            //første kan ikke være før født
            if (compareDateString(person.getFodt(), person.getfKontakt())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }/*
            if(fodt.after(fKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }*/
            if (compareDateString(person.getfKontakt(), person.getDod())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEtterDod"));
            }/*
            if (fKontakt.after(dod)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEtterDod"));
            }*/
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
            if (compareDateString(person.getfKontakt(), person.getsKontakt())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }
            /*
            if (fKontakt.after(sKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }*/

            //første kan ikke være før født
            if (compareDateString(person.getFodt(), person.getfKontakt())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }
            /*
            if(fodt.after(fKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }*/
        }
        //bare siste kontaktdato registrert
        else if (sjekk(person.getsKontakt())) {
            Date sKontakt = getDate(person.getsKontakt());
            if (compareDateString(person.getFodt(), person.getsKontakt())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("sKontakt", "sKontaktForFodt"));
            }
            /*
            if(fodt.after(sKontakt)) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktForFodt"));
            }*/

        }
        //bare første kontaktdato registrert
        else if (sjekk(person.getfKontakt())) {
            Date fKontakt = getDate(person.getfKontakt());
            //første kan ikke være før født
            if (compareDateString(person.getFodt(), person.getfKontakt())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }
            /*
            if(fodt.after(fKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }*/
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
            if (compareDateString(person.getfKontakt(), person.getsKontakt())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }
            /*
            if (fKontakt.after(sKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }*/

            //siste kan ikke være etter død
            if (compareDateString(person.getsKontakt(), person.getDod())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }
            /*
            if(sKontakt.after(dod)) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }*/
        }
        //bare siste kontaktdato registrert
        else if (sjekk(person.getsKontakt())) {
            Date sKontakt = getDate(person.getsKontakt());
            if (compareDateString(person.getsKontakt(), person.getDod())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }
            /*
            if(sKontakt.after(dod)) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }*/

        }
        //bare første kontaktdato registrert
        else if (sjekk(person.getfKontakt())) {
            Date fKontakt = getDate(person.getfKontakt());
            //første kan ikke være etter død
            if (compareDateString(person.getfKontakt(), person.getDod())==DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEtterDod"));
            }
            /*
            if (fKontakt.after(dod)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEtterDod"));
            }*/
        }
        return feil;
    }

    public static boolean isOnlyYearPresent(String dato1, String dato2){
        if (isYearOnly(dato1)||isYearOnly(dato2)){
            return true;
        }
        return false;
    }

    public static int getYear(String dato){
        Calendar c = Calendar.getInstance();
        c.setTime(GyldigeDatoformater.getDate(dato));
        return c.get(Calendar.YEAR);
    }

    /**
     * Sammenligner datostrenger, tar høyde for at det kan kun være årstall (da sammenlignes kun år)
     *
     * returnerer:
     *
     * -1 hvis dato1 < dato2
     *  0 hvis dato1 == dato2
     * +1 hvis dato1 > dato2
     *
     * @param dato1
     * @param dato2
     * @return
     */
    public static DateCompareResult compareDateString(String dato1, String dato2){
        if (isOnlyYearPresent(dato1, dato2)){
            int y1 = getYear(dato1);
            int y2 = getYear(dato2);
            if (y1<y2) return DateCompareResult.BEFORE;
            if (y1==y2) return DateCompareResult.EQUAL;
            if (y1>y2) return DateCompareResult.AFTER;
        }
        else{
            Date d1 = getDate(dato1);
            Date d2 = getDate(dato2);

            int v = d1.compareTo(d2);
            if (v==-1){
                return DateCompareResult.BEFORE;
            }
            if (v==0)
                return DateCompareResult.EQUAL;
            if (v==1)
                return DateCompareResult.AFTER;

        }
        return null;
    }

    static enum DateCompareResult{
        BEFORE,
        EQUAL,
        AFTER
    }

    public static Date getDate(String dato){
        return GyldigeDatoformater.getDate(dato);
    }

    /**
     * Sjekker om dato kun inneholder 4 siffer
     * @param dato
     * @return
     */
    public static boolean isYearOnly(String dato){
        try {
            Integer.parseInt(dato);
            return dato.length() == 4;
        }catch (NumberFormatException e){
            return false;
        }
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
