package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ValidatorTest {

    @Test
    public void validator_nullObjekt_skalGiEnFeil() {
        ArrayList<Valideringsfeil> feil = 
                new Validator<PersondataDTO>(PersondataDTO.class, null).valider();
        
        assertEquals(1, feil.size());
    }
    
    @Test
    public void validator_tomDto_skalGiIngenFeil() {
        PersondataDTO person = new PersondataDTO();
        
        ArrayList<Valideringsfeil> feil = 
                new Validator<PersondataDTO>(PersondataDTO.class, person).valider();
        
        assert(feil.isEmpty());
    }
    
    @Test
    public void validator_DtoMedData_skalGiNullFeil() {
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
        person.setFKontakt("1.1.1999");
        person.setSKontakt("5.1.1999");
        
        ArrayList<Valideringsfeil> feil = 
                new Validator<PersondataDTO>(PersondataDTO.class, person).valider();
        
        assert(feil.isEmpty());
    }
}
