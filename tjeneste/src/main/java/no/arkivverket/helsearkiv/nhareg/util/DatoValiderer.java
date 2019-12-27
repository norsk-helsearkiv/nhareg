package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;

import java.util.*;

/**
 * Validerer konsistens på datofeltene til PersondataDTO
 * Benyttes i POST og PUt
 * @author robing
 */
public class DatoValiderer {

    private final List<String> gyldigMors = Arrays.asList("mors", "m", "M");
    private final List<String> gyldigUkjent = Arrays.asList("ukjent", "u", "U");

    /**
     * Diagnosedato/år valideres med javax.validation
     *
     * @param diagnose
     * @param pasientjournal
     * @return
     */
    public List<Valideringsfeil> validerDiagnose(DiagnoseDTO diagnose, Pasientjournal pasientjournal) {
        final List<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        final Grunnopplysninger grunnopplysninger = pasientjournal.getGrunnopplysninger();

        //ta diagnosedato som god fisk ettersom både mors og født er ukjent...
        if (grunnopplysninger.getFodtdatoUkjent() != null &&
            grunnopplysninger.getFodtdatoUkjent() &&
            grunnopplysninger.getDødsdatoUkjent() != null &&
            grunnopplysninger.getDødsdatoUkjent()) {
            return feil;
        }

        final String diagnoseDatoString = diagnose.getDiagnosedato();
        final Date diagnoseDato = getDate(diagnoseDatoString);

        if (diagnoseDato == null) {
            feil.add(new Valideringsfeil("diagnosedato", "DiagFormatFeil"));
            return feil;
        }

        //fødtdatoår kjent
        if (grunnopplysninger.getFodtdatoUkjent() == null || !grunnopplysninger.getFodtdatoUkjent()) {
            final DatoEllerAar fodt = grunnopplysninger.getFødt();
            final String fodtString = DateOrYearConverter.fromDateOrYear(fodt);
            if ((compareDateString(fodtString, diagnoseDatoString) == DateCompareResult.AFTER)) {
                feil.add(new Valideringsfeil("diagnosedato", "DiagForFodt"));
            }
        }

        if (grunnopplysninger.getDødsdatoUkjent() == null || !grunnopplysninger.getDødsdatoUkjent()) {
            final DatoEllerAar dod = grunnopplysninger.getDød();
            final String dodString = DateOrYearConverter.fromDateOrYear(dod);
            if ((compareDateString(dodString, diagnoseDatoString) == DateCompareResult.BEFORE)) {
                feil.add(new Valideringsfeil("diagnosedato", "DiagEtterDod"));
            }
        }

        return feil;
    }

    //Hjelpemetoder for validering
    public ArrayList<Valideringsfeil> valider(PersondataDTO person, ConfigurationDAO konfig) {
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();

        if (person == null) {
            return feil;
        }

        //født kan ikke være mors
        if (sjekkMorsUkjent(person.getFodt(), "mors") &&
            sjekkMorsUkjent(person.getFodt(), "m")) {
            feil.add(new Valideringsfeil("fodt", "DagEllerAar"));
        }

        //død kan ikke være ukjent
        if (sjekkMorsUkjent(person.getDod(), "ukjent") &&
            sjekkMorsUkjent(person.getDod(), "u")) {
            feil.add(new Valideringsfeil("dod", "DagEllerAar"));
        }

        if (feil.size() > 0) {
            return feil;
        }

        Date lowLim = konfig.getDate(ConfigurationDAO.KONFIG_LOWLIM);
        Integer waitLim = konfig.getInt(ConfigurationDAO.KONFIG_WAITLIM);
        Date maxLim = GyldigeDatoformater.getDateRoll(new Date(), -waitLim);
        Integer maxAge = konfig.getInt(ConfigurationDAO.KONFIG_MAXAGE);
        Date minLim = GyldigeDatoformater.getDateRoll(new Date(), -maxAge);

        //skjema 1
        List<Valideringsfeil> fnumfeil = fnumSjekk(person, lowLim, maxLim);
        leggTilFeil(feil, fnumfeil);

        //skjema 2a
        if (gyldigUkjent.contains(person.getFodt())) { //mors sjekkes i metoden pga feilmelding hvis den mangler
            List<Valideringsfeil> pdatofeil = pasientjournaldatoerutenkjentfogmors(person, lowLim, maxLim);
            leggTilFeil(feil, pdatofeil);
        }

        //skjema 2b
        if (gyldigMors.contains(person.getDod()) && sjekk(person.getFodt())) {
            List<Valideringsfeil> pdatofeilb = pasientjournaldatokjentfdatoukjentdod(person, minLim);
            leggTilFeil(feil, pdatofeilb);
        }

        //skjema 2c
        if (gyldigUkjent.contains(person.getFodt()) && sjekk(person.getDod())) {
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

    private void leggTilFeil(List<Valideringsfeil> feilliste, List<Valideringsfeil> nyeFeil) {
        if (!nyeFeil.isEmpty()) {
            feilliste.addAll(nyeFeil);
        }
    }

    //skjema 01
    private ArrayList<Valideringsfeil> fnumSjekk(PersondataDTO person, Date lowLim, Date maxLim) {
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        String fnr = person.getFodselsnummer();

        if (!gyldigUkjent.contains(person.getFodt())) {
            if (fnr == null || fnr.isEmpty()) {
                //sjekk fødselsdato
                if (sjekk(person.getFodt())) {
                    //TODO sjekke også her på år?
                    Date fodt = getDate(person.getFodt());
                    if (fodt.before(lowLim) || fodt.after(maxLim)) {
                        feil.add(new Valideringsfeil("fodt", "UtenforGyldigPeriode",
                                                     "Person born outside valid period."));
                    }
                }
            } else {
                Valideringsfeil fnrfeil = PersonnummerValiderer.valider(person);
                if (fnrfeil != null) {
                    feil.add(fnrfeil);
                }
            }
        }

        return feil;
    }

    //skjema 02a
    private ArrayList<Valideringsfeil> pasientjournaldatoerutenkjentfogmors(PersondataDTO person, Date lowLim, Date maxLim) {
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        if (gyldigMors.contains(person.getDod())){
            if (sjekk(person.getSKontakt()) && sjekk(person.getFKontakt())) {
                Date sKontakt = getDate(person.getSKontakt());
                if ((compareDateString(person.getFKontakt(), person.getSKontakt())) == DateCompareResult.AFTER) {
                    feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
                }

                //TODO sammenligne disse også på kun ÅR?
                if (sKontakt.before(lowLim) || sKontakt.after(maxLim)) {
                    feil.add(new Valideringsfeil("sKontakt", "UtenforGyldigPeriode"));
                }
            }
        } else if (!sjekk(person.getDod())) {
            feil.add(new Valideringsfeil("dod", "manglermors"));
        }

        return feil;
    }

    //skjema 02b
    private List<Valideringsfeil> pasientjournaldatokjentfdatoukjentdod(PersondataDTO person, Date maxAge) {
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        if (sjekk(person.getDod()) || gyldigMors.contains(person.getDod())) {
            List<Valideringsfeil> f = sjekkKontaktdatoFodt(person);
            if (!f.isEmpty()) {
                feil.addAll(f);
            }
        } else {
            if (sjekk(person.getFodt())) {
                Date fodt = getDate(person.getFodt());
                //TODO også her kun på år?
                if (fodt.before(maxAge)) {
                    feil.add(new Valideringsfeil("fodt", "UtenforGyldigPeriode"));
                } else {
                    List<Valideringsfeil> f = sjekkKontaktdatoFodt(person);
                    if (!f.isEmpty()) {
                        feil.addAll(f);
                    }
                }
            } else {
                //
                //TODO. født mangler, håndteres ved kalling av funksjo.. mao vi kommer ikke hit..
            }
        }
     
        return feil;
    }

    //skjema 02c
    private List<Valideringsfeil> pasientjournaldatokjentmorsukjentfodt(PersondataDTO person, Date lowLim, Date maxLim) {
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        Date dod = getDate(person.getDod());
        //TODO også her sammenlign på år?
        if (dod.before(lowLim) || dod.after(maxLim)) {
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
    private List<Valideringsfeil> pasientjournaldatokjentfodtogmors(PersondataDTO person, Date lowLim, Date maxLim){
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        Date dod = getDate(person.getDod());
        //TODO også her sammenligne på ÅR?
        if (dod.before(lowLim) || dod.after(maxLim)) {
            feil.add(new Valideringsfeil("dod", "UtenforGyldigPeriode"));
        } else {
            if (sjekk(person.getFodt()) && sjekk(person.getDod())) {
                Date fodt = getDate(person.getFodt());
                if (compareDateString(person.getDod(), person.getFodt()) == DateCompareResult.BEFORE) {
                    feil.add(new Valideringsfeil("fodt", "FodtEtterDodt"));
                } else {
                    List<Valideringsfeil> f = sjekkKontaktdatoFodtogDod(person);
                    if (!f.isEmpty()) {
                        feil.addAll(f);
                    }
                }
            }
        }

        return feil;
    }

    private List<Valideringsfeil> sjekkKontaktdatoFodtogDod(PersondataDTO person) {
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();

        //første og siste kontaktdato registrert
        if (sjekk(person.getFKontakt()) && sjekk(person.getSKontakt())) {
            Date fKontakt = getDate(person.getFKontakt());
            Date sKontakt = getDate(person.getSKontakt());

            if (compareDateString(person.getFKontakt(), person.getSKontakt()) == DateCompareResult.AFTER) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }

            //første kan ikke være før født
            if (compareDateString(person.getFodt(), person.getFKontakt()) == DateCompareResult.AFTER) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }

            if (compareDateString(person.getSKontakt(), person.getDod()) == DateCompareResult.AFTER) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }
        }
        //bare siste kontaktdato registrert
        else if (sjekk(person.getSKontakt())) {
            Date sKontakt = getDate(person.getSKontakt());
            if (compareDateString(person.getFodt(), person.getSKontakt()) == DateCompareResult.AFTER) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktForFodt"));
            }

            if (compareDateString(person.getSKontakt(), person.getDod()) == DateCompareResult.AFTER) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }
        }
        //bare første kontaktdato registrert
        else if (sjekk(person.getFKontakt())) {
            Date fKontakt = getDate(person.getFKontakt());
            //første kan ikke være før født
            if (compareDateString(person.getFodt(), person.getFKontakt()) == DateCompareResult.AFTER) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }

            if (compareDateString(person.getFKontakt(), person.getDod()) == DateCompareResult.AFTER) {
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
    private List<Valideringsfeil> sjekkKontaktdatoFodt(PersondataDTO person){
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        //første og siste kontaktdato registrert
        if (sjekk(person.getFKontakt()) && sjekk(person.getSKontakt())) {
            if (compareDateString(person.getFKontakt(), person.getSKontakt()) == DateCompareResult.AFTER) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }

            //første kan ikke være før født
            if (compareDateString(person.getFodt(), person.getFKontakt()) == DateCompareResult.AFTER) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktForFodt"));
            }
        }
        //bare siste kontaktdato registrert
        else if (sjekk(person.getSKontakt())) {
            if (compareDateString(person.getFodt(), person.getSKontakt()) == DateCompareResult.AFTER) {
                feil.add(new Valideringsfeil("sKontakt", "sKontaktForFodt"));
            }

        }
        //bare første kontaktdato registrert
        else if (sjekk(person.getFKontakt())) {
            //første kan ikke være før født
            if (compareDateString(person.getFodt(), person.getFKontakt()) == DateCompareResult.AFTER) {
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
    private List<Valideringsfeil> sjekkKontaktdatoDod(PersondataDTO person){
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        Date dod = getDate(person.getDod());
        //første og siste kontaktdato registrert
        if (sjekk(person.getFKontakt()) && sjekk(person.getSKontakt())) {
            if (compareDateString(person.getFKontakt(), person.getSKontakt()) == DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }

            //siste kan ikke være etter død
            if (compareDateString(person.getSKontakt(), person.getDod()) == DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }
        }
        //bare siste kontaktdato registrert
        else if (sjekk(person.getSKontakt())) {
            if (compareDateString(person.getSKontakt(), person.getDod()) == DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
            }

        }
        //bare første kontaktdato registrert
        else if (sjekk(person.getFKontakt())) {
            //første kan ikke være etter død
            if (compareDateString(person.getFKontakt(), person.getDod()) == DateCompareResult.AFTER){
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEtterDod"));
            }
        }
        return feil;
    }

    public boolean isOnlyYearPresent(String dato1, String dato2) {
        if (isYearOnly(dato1) || isYearOnly(dato2)) {
            return true;
        }
        return false;
    }

    public int getYear(String dato) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(dato));
        return calendar.get(Calendar.YEAR);
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
    public DateCompareResult compareDateString(String dato1, String dato2) {
        if (isOnlyYearPresent(dato1, dato2)) {
            int y1 = getYear(dato1);
            int y2 = getYear(dato2);
            if (y1 < y2) return DateCompareResult.BEFORE;
            if (y1 == y2) return DateCompareResult.EQUAL;
            return DateCompareResult.AFTER;
        } else {
            Date d1 = getDate(dato1);
            Date d2 = getDate(dato2);

            int v = d1.compareTo(d2);
            if (v < 0){
                return DateCompareResult.BEFORE;
            }
            if (v == 0)
                return DateCompareResult.EQUAL;
            
            return DateCompareResult.AFTER;
        }
    }

    enum DateCompareResult{
        BEFORE,
        EQUAL,
        AFTER
    }

    private Date getDate(String dato) {
        return GyldigeDatoformater.getDate(dato);
    }

    /**
     * Sjekker om dato kun inneholder 4 siffer
     * @param dato
     * @return
     */
    private boolean isYearOnly(String dato) {
        try {
            Integer.parseInt(dato);
            return dato.length() == 4;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean sjekk(String s) {
        if (s == null || s.isEmpty()
            || s.toLowerCase().equals("mors") || s.toLowerCase().equals("m")
            || s.toLowerCase().equals("ukjent") || s.toLowerCase().equals("u"))  {
            return false;
        }

        return getDate(s) != null;
    }

    private boolean sjekkMorsUkjent(String s, String toCheck) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        return (s.toLowerCase().equals(toCheck));
    }
}
