package no.arkivverket.helsearkiv.nhareg.tjeneste;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AvtaleTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private AvtaleTjeneste tjeneste;

    @Test
    public void create() {
        Avtale avtale = new Avtale();
        avtale.setAvtaleidentifikator("Avtale100");
        Response response = tjeneste.create(avtale);
        assertNotNull(response);
    }

    @Test
    public void testAvtaleMedId() {

        // Test loading a single venue
        Avtale avtale = tjeneste.getSingleInstance("A1234");
        assertNotNull(avtale);
        assertEquals("A1234", avtale.getAvtaleidentifikator());
    }

    @Test
    public void testPagination() {

        // Test pagination logic
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();

        List<Avtale> beskrivelser = tjeneste.getAll(queryParameters);
        assertNotNull(beskrivelser);
        assertEquals(3, beskrivelser.size());
//        assertEquals("ArkivTestID1", beskrivelser.get(0).getArkivID());
    }

    @Test
    public void getAvleveringer() {
        List<Avlevering> avleveringer = tjeneste.getAvleveringer("A1234");
        assertNotNull(avleveringer);
        assertFalse(avleveringer.isEmpty());
        assertEquals(1, avleveringer.size());
    }
}
