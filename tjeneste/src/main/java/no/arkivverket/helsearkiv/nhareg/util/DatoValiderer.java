package no.arkivverket.helsearkiv.nhareg.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;

/**
 * Validerer konsistens på datofeltene til PersondataDTO
 * Benyttes i POST og PUt
 * @author robing
 */
public class DatoValiderer {
    //Hjelpemetoder for validering
    public static ArrayList<Valideringsfeil> valider(PersondataDTO person) throws ParseException {
        ArrayList<Valideringsfeil> feil = new ArrayList<Valideringsfeil>();
        
        //Regler basert på født
        if(sjekk(person.getFodt())) {
            Date fodt = getDate(person.getFodt());
            
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
        
        //Regler basert på død
        if(sjekk(person.getDod())) {
            Date dod = getDate(person.getDod());
            
            if(sjekk(person.getfKontakt())) {
                Date fKontakt = getDate(person.getfKontakt());
                if(fKontakt.after(dod)) {
                    feil.add(new Valideringsfeil("fKontakt", "fKontaktEtterDod"));
                }
            }
            
            if(sjekk(person.getsKontakt())) {
                Date sKontakt = getDate(person.getsKontakt());
                if(sKontakt.after(dod)) {
                    feil.add(new Valideringsfeil("sKontakt", "sKontaktEtterDod"));
                }
            }
        }
        
        if(sjekk(person.getfKontakt()) && sjekk(person.getsKontakt())) {
            Date fKontakt = getDate(person.getfKontakt());
            Date sKontakt = getDate(person.getsKontakt());
            
            if(fKontakt.after(sKontakt)) {
                feil.add(new Valideringsfeil("fKontakt", "fKontaktEttersKontakt"));
            }
        }
        return feil;
    }
    
    private static Date getDate(String dato){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
        SimpleDateFormat aar = new SimpleDateFormat("yyyy");
        
        try {
            if(dato.length() == 4) {
                return aar.parse(dato);
            } else {
                return sdf.parse(dato);
            }    
        } catch(ParseException pe) {
            return null;
        }
        
    }
    
    private static boolean sjekk(String s) {
        if(s == null || s.isEmpty() || s.toLowerCase().equals("mors")) {
            return false;
        }
        
        return getDate(s) != null;
    }
}
