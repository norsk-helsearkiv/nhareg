package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.AvleveringDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.util.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.*;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class AvleveringTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private AvleveringTjeneste tjeneste;

    @Test
    public void validerLagringsenheter() {
        List<Lagringsenhet> ll = new ArrayList<Lagringsenhet>();
        Lagringsenhet la = new Lagringsenhet();
        la.setIdentifikator("boks1");
        ll.add(la);
        String av = "Avlevering-1";
        assertEquals(0, tjeneste.validerLagringsenheter(av, ll).size());
        av = "Annen";
        assertEquals(1, tjeneste.validerLagringsenheter(av, ll).size());
    }

    @Test
    public void nyPasientjournal_oppretterNyPasientjournal_200() throws ParseException {
        String avleveringsid = "Avlevering-1";
        PersondataDTO pasient = getPasient();
        PasientjournalDTO nyPasient = (PasientjournalDTO) tjeneste.nyPasientjournal(avleveringsid, pasient).getEntity();
        assertNotNull(nyPasient.getPersondata().getUuid());
    }

    @Test
    public void delete_sletterAvleveringUtenPasientjournaler_200() {
        Avlevering avlevering = tjeneste.delete("Avlevering-2");
        assertNotNull(avlevering);
    }

    @Test(expected = ValideringsfeilException.class)
    public void delete_sletterAvleveringMedPasientjournaler_409() {
        tjeneste.delete("Avlevering-1");
    }

    @Test
    public void getPasientjournaler_henterPasienjounaler_utenLagringsenhetOgDiagnoser() {
        ListeObjekt lstObj = tjeneste.getPasientjournaler("Avlevering-1", getInfo());
        assertNotNull(lstObj);

        List<PasientjournalSokeresultatDTO> liste = (List<PasientjournalSokeresultatDTO>) lstObj.getListe();
        assertTrue(liste.size() > 0);
    }

    @Test
    public void updateAvlevering() {
        String id = "Avlevering-1";
        Avlevering avlevering = tjeneste.getSingleInstance(id);
        assertNotNull(avlevering);
        assertNotNull(avlevering.getAvtale());
        
        int apj = avlevering.getPasientjournal().size();
        
        AvleveringDTO avleveringDTO = new AvleveringDTO(avlevering);
        avleveringDTO.setArkivskaper("JUnit test");
        
        avleveringDTO = tjeneste.updateAvlevering(avleveringDTO);
        assertNotNull(avleveringDTO);
        //
        // sjekker at antall pasientjournaler er det samme
        //
        avlevering = tjeneste.getSingleInstance(id);
        assertNotNull(avlevering);
        assertNotNull(avlevering.getAvtale());
        assertNotNull(avlevering.getOppdateringsinfo());
        assertEquals(apj, avlevering.getPasientjournal().size());
    }

    private PersondataDTO getPasient() {
        PersondataDTO pasient = new PersondataDTO();
        pasient.setDod("2000");
        pasient.setFodselsnummer("01019912345");
        pasient.setFodt("1.1.1999");
        pasient.setJournalnummer("123");
        pasient.setKjonn("K");
        String[] lagringsenheter = {"boks-1"};
        pasient.setLagringsenheter(lagringsenheter);
        pasient.setLopenummer("2345");
        pasient.setNavn("Nora");
        pasient.setfKontakt("1.1.1999");
        pasient.setsKontakt("2000");
        return pasient;
    }

    private UriInfo getInfo() {
        return new UriInfo() {

            public String getPath() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public String getPath(boolean bln) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public List<PathSegment> getPathSegments() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public List<PathSegment> getPathSegments(boolean bln) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public URI getRequestUri() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public UriBuilder getRequestUriBuilder() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public URI getAbsolutePath() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public UriBuilder getAbsolutePathBuilder() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public URI getBaseUri() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public UriBuilder getBaseUriBuilder() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public MultivaluedMap<String, String> getPathParameters() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public MultivaluedMap<String, String> getPathParameters(boolean bln) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public MultivaluedMap<String, String> getQueryParameters() {
                return new MultivaluedHashMap<String, String>();
            }

            public MultivaluedMap<String, String> getQueryParameters(boolean bln) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public List<String> getMatchedURIs() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public List<String> getMatchedURIs(boolean bln) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public List<Object> getMatchedResources() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public URI resolve(URI uri) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public URI relativize(URI uri) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
}
