package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.util.RESTDeployment;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.inject.Inject;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AvleveringTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private AvleveringTjeneste tjeneste;
    @Test
    public void validerLagringsenheter(){
        List<Lagringsenhet> ll = new ArrayList<Lagringsenhet>();
        Lagringsenhet la = new Lagringsenhet();
        la.setIdentifikator("boks1");
        ll.add(la);
        String av = "Avlevering-1";
        assertEquals(0,tjeneste.validerLagringsenheter(av, ll).size());
        av = "Annen";
        assertEquals(1,tjeneste.validerLagringsenheter(av, ll).size());        
    }

    @Test
    public void nyPasientjournal_oppretterNyPasientjournal_200() throws ParseException {
        String avleveringsid = "Avlevering-1";
        PersondataDTO pasient = getPasient();
        Pasientjournal nyPasient = (Pasientjournal) tjeneste.nyPasientjournal(avleveringsid, pasient).getEntity();
        assertTrue(nyPasient.getUuid() != null);
    }
    
    @Test
    public void delete_sletterAvleveringUtenPasientjournaler_200() {
        Avlevering avlevering = tjeneste.delete("Avlevering-2");
        assertNotNull(avlevering);
    }
    
    @Test
    public void delete_sletterAvleveringMedPasientjournaler_409() {
        try {
            tjeneste.delete("Avlevering-1");
        } catch(EJBException e) {
            assertEquals(ValideringsfeilException.class, e.getCause().getClass());
        }
    }
    
    private PersondataDTO getPasient() {
        PersondataDTO pasient = new PersondataDTO();
        pasient.setDod("2000");
        pasient.setFodselsnummer("01019912345");
        pasient.setFodt("1.1.1999");
        pasient.setJournalnummer("123");
        pasient.setKjonn("K");
        String[] lagringsenheter = { "boks-1" };
        pasient.setLagringsenheter(lagringsenheter);
        pasient.setLopenummer("2345");
        pasient.setNavn("Nora");
        pasient.setfKontakt("1.1.1999");
        pasient.setsKontakt("2000");
        return pasient;
    }
    
}
