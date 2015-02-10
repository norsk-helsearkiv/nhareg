package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.text.ParseException;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
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
    public void nyPasientjournal_oppretterNyPasientjournal_200() throws ParseException {
        String avleveringsid = "Avlevering-1";
        PersondataDTO pasient = getPasient();
        Pasientjournal nyPasient = (Pasientjournal) tjeneste.nyPasientjournal(avleveringsid, pasient).getEntity();
        assertTrue(nyPasient.getUuid() != null);
    }
    
    @Test
    public void delete_sletterAvleveringUtenPasientjournaler_200() {
        Response rsp = tjeneste.delete("Avlevering-2");
        assertEquals(200, rsp.getStatus());
    }
    
    @Test
    public void delete_sletterAvleveringMedPasientjournaler_409() {
        Response rsp = tjeneste.delete("Avlevering-1");
        assertEquals(409, rsp.getStatus());
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
