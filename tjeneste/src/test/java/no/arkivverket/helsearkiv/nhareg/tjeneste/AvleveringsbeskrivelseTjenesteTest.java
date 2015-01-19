package no.arkivverket.helsearkiv.nhareg.tjeneste;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import javax.ejb.EJBException;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import no.arkivverket.helsearkiv.nhareg.domene.avleveringsdokumentasjon.Avleveringsbeskrivelse;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AvleveringsbeskrivelseTjenesteTest {
    
    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }
   
    @Inject
    private AvleveringsbeskrivelseTjeneste tjeneste;
    
    @Test(expected = EJBException.class)
    public void testAvleveringsbeskrivelseMedId() {
        
        // Test loading a single venue
        Avleveringsbeskrivelse beskrivelse = tjeneste.getSingleInstance("A12345");
        assertNotNull(beskrivelse);
        assertEquals("A12345", beskrivelse.getArkivID());
    }
    
    @Test
    public void testPagination() {
        
        // Test pagination logic
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();

        queryParameters.add("first", "2");
        queryParameters.add("maxResults", "1");
        
        List<Avleveringsbeskrivelse> beskrivelser = tjeneste.getAll(queryParameters);
        assertNotNull(beskrivelser);
        assertEquals(0, beskrivelser.size());
//        assertEquals("ArkivTestID1", beskrivelser.get(0).getArkivID());
    }
    @Test
    public void testCreate(){
        Avleveringsbeskrivelse a = new Avleveringsbeskrivelse();
        a.setAvleveringsidentifikator("Avlevering 001");
        tjeneste.createAvleveringsbeskrivelse(a);
    }
}
