package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.Calendar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import javax.ejb.EJBException;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PasientjournalTjenesteTest {
    
    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }
   
    @Inject
    private PasientjournalTjeneste tjeneste;
    
    @Test
    public void testPagination() {
        
        // Test pagination logic
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();

        queryParameters.add("first", "2");
        queryParameters.add("maxResults", "1");
        
        List<Pasientjournal> beskrivelser = tjeneste.getAll(queryParameters);
        assertNotNull(beskrivelser);
        assertEquals(0, beskrivelser.size());
    }
    
    @Test
    public void testCreate(){
        Pasientjournal a = new Pasientjournal();
        tjeneste.create(a);
    }
}
