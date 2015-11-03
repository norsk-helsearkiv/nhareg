package no.arkivverket.helsearkiv.nhareg.util;

import java.text.ParseException;
import java.util.ArrayList;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author robing
 */
public class DatoValidererTest {
    
    @Test
    public void testKonsistensIDato() throws ParseException {
       /* PersondataDTO person = new PersondataDTO();
        ArrayList<Valideringsfeil> feil;
        
        person.setFodt("2000");
        person.setDod("1999");
        feil = DatoValiderer.valider(person);
        assertFalse(feil.isEmpty());
        
        person.setFodt("1.1.2000");
        person.setDod("1.1.2000");
        person.setfKontakt("1.1.2000");
        person.setsKontakt("1.1.2000");
        feil = DatoValiderer.valider(person);
        assertTrue(feil.isEmpty());
        
        person.setFodt("1.1.2000");
        person.setDod("1.1.1999");
        person.setfKontakt("1.1.1999");
        person.setsKontakt("1.1.1999");
        feil = DatoValiderer.valider(person);
        assertFalse(feil.isEmpty());
        
        person.setFodt("1.1.2000");
        person.setfKontakt("1.1.1999");
        person.setsKontakt("1.1.1999");
        feil = DatoValiderer.valider(person);
        assertFalse(feil.isEmpty());
        
        person.setFodt("");
        person.setfKontakt("1.1.1999");
        person.setsKontakt("1.1.1999");
        feil = DatoValiderer.valider(person);
        assertTrue(feil.isEmpty());*/
    }
}
