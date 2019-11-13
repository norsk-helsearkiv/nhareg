package no.arkivverket.helsearkiv.nhareg.tjeneste;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.AvleveringDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.utilities.AdminHandler;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import no.arkivverket.helsearkiv.nhareg.utilities.UserHandler;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class AvleveringTjenesteTest {

    private static Logger log = Logger.getLogger(AvleveringTjenesteTest.class);

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private AvleveringTjeneste tjeneste;

    @Inject
    private AdminHandler adminHandler;

    @Inject
    private UserHandler userHandler;

    @Test
    public void avleveringsTest_validerLagringsenheter_nullFeil() throws Exception {
        userHandler.call(new Callable() {
            @Override
            public Object call() {
                List<Lagringsenhet> lagringsenhetListe = new ArrayList<Lagringsenhet>();
                Lagringsenhet lagringsenhet = new Lagringsenhet();
                final String avlevering = "Avlevering-1";

                lagringsenhet.setIdentifikator("boks1");
                lagringsenhetListe.add(lagringsenhet);

                assertNotNull(tjeneste);
                List<Valideringsfeil> valideringsfeil = tjeneste.validerLagringsenheter(avlevering, lagringsenhetListe);
                assertEquals(0, valideringsfeil.size());

                return null;
            }
        });
    }

    @Test
    public void avleveringsTest_validerLagringsenheter_enFeil() throws Exception {
        userHandler.call(new Callable() {
            @Override
            public Object call() {
                List<Lagringsenhet> lagringsenhetListe = new ArrayList<Lagringsenhet>();
                Lagringsenhet lagringsenhet = new Lagringsenhet();
                lagringsenhet.setIdentifikator("boks1");
                lagringsenhetListe.add(lagringsenhet);

                String avlevering = "Avlevering-1";
                final List<Valideringsfeil> nullFeil = tjeneste.validerLagringsenheter(avlevering, lagringsenhetListe);
                assertEquals(0, nullFeil.size());

                avlevering = "SkalFeile";
                final List<Valideringsfeil> enFeil =  tjeneste.validerLagringsenheter(avlevering, lagringsenhetListe);
                assertEquals(1, enFeil.size());

                return null;
            }
        });
    }

    @Test
    public void avleveringsTest_nyPasientjournal_utenFeil() throws Exception {
        adminHandler.call(new Callable() {
            @Override
            public Object call() throws ParseException {
                final String avleveringsid = "Avlevering-1";
                final PersondataDTO persondataDTO = getPasient();
                final Response response = tjeneste.nyPasientjournal(avleveringsid, persondataDTO);
                final PasientjournalDTO nyPasient = (PasientjournalDTO) response.getEntity();

                assertNotNull(nyPasient.getPersondata().getUuid());
                
                return null;
            }
        });
    }

    @Test
    public void avleveringsTest_sletterAvleveringUtenPasientjournaler_skalIkkeKasteException() throws Exception {
        adminHandler.call(new Callable() {
            @Override
            public Object call() {
                Avlevering avlevering = tjeneste.delete("Avlevering-2");
                assertNotNull(avlevering);
                
                return null;
            }
        });
    }

    @Test(expected = ValideringsfeilException.class)
    public void avleveringsTest_sletterAvleveringMedPasientjournaler_skalKasteException() throws Exception {
        adminHandler.call(new Callable<Object>() {
            @Override
            public Object call() throws ValideringsfeilException {
                tjeneste.delete("Avlevering-1");
                
                return null;
            }
        });
    }

    @Test
    public void avleveringsTest_getPasientjournaler_skalGiIkkeTomListe() throws Exception {
        adminHandler.call(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                ListeObjekt pasientjournaler = tjeneste.getPasientjournaler("Avlevering-1", getInfo());
                assertNotNull(pasientjournaler);
        
                List<PasientjournalSokeresultatDTO> liste = (List<PasientjournalSokeresultatDTO>) pasientjournaler.getListe();
                assertTrue(liste.size() > 0);
                
                return null;
            }
        });
    }

    @Test
    public void avleveringsTest_updateAvlevering_pasientJournalSizeIkkeEndret() throws Exception {
        adminHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                String id = "Avlevering-1";
                Avlevering avlevering = tjeneste.getSingleInstance(id);
                assertNotNull(avlevering);
                assertNotNull(avlevering.getAvtale());
        
                int pasientjournalSize = avlevering.getPasientjournal().size();
        
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
                assertEquals(pasientjournalSize, avlevering.getPasientjournal().size());
                
                return null;
            }
        });
    }

    private PersondataDTO getPasient() {
        PersondataDTO pasient = new PersondataDTO();
        String[] lagringsenheter = { "boks-1" };
        pasient.setLagringsenheter(lagringsenheter);
        pasient.setFanearkid("123456789100");
        pasient.setJournalnummer("123");
        pasient.setLopenummer("2345");
        pasient.setFodselsnummer("19090040165");
        pasient.setNavn("Nora");
        pasient.setKjonn("K");
        pasient.setFodt("1.1.1999");
        pasient.setDod("2000");
        pasient.setfKontakt("1.1.1999");
        pasient.setsKontakt("2000");
        pasient.setUuid("1234");
        
        return pasient;
    }
    
    private UriInfo getInfo() {
        return new UriInfo() {
            @Override
            public String getPath() {
                return null;
            }

            @Override
            public String getPath(final boolean decode) {
                return null;
            }

            @Override
            public List<PathSegment> getPathSegments() {
                return null;
            }

            @Override
            public List<PathSegment> getPathSegments(final boolean decode) {
                return null;
            }

            @Override
            public URI getRequestUri() {
                return null;
            }

            @Override
            public UriBuilder getRequestUriBuilder() {
                return null;
            }

            @Override
            public URI getAbsolutePath() {
                return null;
            }

            @Override
            public UriBuilder getAbsolutePathBuilder() {
                return null;
            }

            @Override
            public URI getBaseUri() {
                return null;
            }

            @Override
            public UriBuilder getBaseUriBuilder() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getPathParameters() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getPathParameters(final boolean decode) {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getQueryParameters() {
                return new MultivaluedHashMap<String, String>();
            }

            @Override
            public MultivaluedMap<String, String> getQueryParameters(final boolean decode) {
                return null;
            }

            @Override
            public List<String> getMatchedURIs() {
                return null;
            }

            @Override
            public List<String> getMatchedURIs(final boolean decode) {
                return null;
            }

            @Override
            public List<Object> getMatchedResources() {
                return null;
            }

            @Override
            public URI resolve(final URI uri) {
                return null;
            }

            @Override
            public URI relativize(final URI uri) {
                return null;
            }
        };
    }
}
