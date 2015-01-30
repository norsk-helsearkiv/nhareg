package no.arkivverket.helsearkiv.nhareg.tjeneste;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
    public void create() {
        Avlevering avlevering = new Avlevering();
        avlevering.setAvleveringsidentifikator("Avlevering100");
        Response response = tjeneste.create(avlevering);
        assertNotNull(response);
    }

    @Test
    public void testAvleveringMedId() {

        // Test loading a single venue
        Avlevering avlevering = tjeneste.getSingleInstance("Avlevering-1");
        assertNotNull(avlevering);
        assertEquals("Avlevering-1", avlevering.getAvleveringsidentifikator());
    }

    @Test
    public void testPagination() {

        // Test pagination logic
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();

        queryParameters.add("first", "1");
        queryParameters.add("max", "1");

        List<Avlevering> beskrivelser = tjeneste.getAll(queryParameters);
        assertNotNull(beskrivelser);
        assertEquals(1, beskrivelser.size());
//        assertEquals("ArkivTestID1", beskrivelser.get(0).getArkivID());
    }
    @Test
    public void getAvleveringer() {
        ListeObjekt pasientjournaler = tjeneste.getPasientjournaler(null, "Avlevering-1");
        List<Pasientjournal> liste = (List<Pasientjournal>) pasientjournaler.getListe();
        assertNotNull(liste);
        assertFalse(liste.isEmpty());
        assertEquals(1, liste.size());
    }
}
