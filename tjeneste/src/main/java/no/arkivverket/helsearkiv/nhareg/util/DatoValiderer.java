package no.arkivverket.helsearkiv.nhareg.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;
import no.arkivverket.helsearkiv.nhareg.tjeneste.KonfigparamTjeneste;

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
        if (sjekkMorsUkjent(person.getDod(), "ukjent")){
            feil.add(new Valideringsfeil("dod", "DagEllerAar"));
        }


        if (feil.size()>0){
            return feil;
        }
        Date lowLim = konfig.getDate(KonfigparamTjeneste.KONFIG_LOWLIM);
        Integer waitLim = konfig.getInt(KonfigparamTjeneste.KONFIG_WAITLIM);
        Date maxLim = GyldigeDatoformater.getDateRoll(new Date(), -waitLim);

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
        
        //Regler basert på død (ikke mors)
        if(sjekk(person.getDod())) {
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
