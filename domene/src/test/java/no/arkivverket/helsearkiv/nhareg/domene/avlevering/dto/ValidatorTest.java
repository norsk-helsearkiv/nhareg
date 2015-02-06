package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import java.util.ArrayList;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidatorTest {

    @Test
    public void valider_nullObjekt_feil() {
        ArrayList<Valideringsfeil> feil = 
                new Validator<PersondataDTO>(PersondataDTO.class, null).valider();
        
        assertEquals(1, feil.size());
    }
    
    @Test
    public void valider_tomtObjekt_feil() {
        PersondataDTO person = new PersondataDTO();
        
        ArrayList<Valideringsfeil> feil = 
                new Validator<PersondataDTO>(PersondataDTO.class, person).valider();
        
        assertFalse(feil.isEmpty());
    }
    
    @Test
    public void valider_objekt_riktig() {
        PersondataDTO person = new PersondataDTO();
        String[] enheter = {"1"};
        person.setLagringsenheter(enheter);
        person.setJournalnummer("123");
        person.setLopenummer("234");
        person.setFodselsnummer("01019912345");
        person.setNavn("Nora");
        person.setKjonn("K");
        person.setFodt("1.1.1999");
        person.setDod("4.1.1999");
        person.setfKontakt("1.1.1999");
        person.setsKontakt("5.1.1999");
        
        ArrayList<Valideringsfeil> feil = 
                new Validator<PersondataDTO>(PersondataDTO.class, person).valider();
        
        assertTrue(feil.isEmpty());
    }
    
    
}
