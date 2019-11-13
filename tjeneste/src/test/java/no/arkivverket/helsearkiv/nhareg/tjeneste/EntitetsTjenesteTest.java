package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.net.URI;
import java.util.List;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tester grunn funksjonaliteten til Entitetstjeneste, basert på det aller
 * enkleste objektet, Avtale
 *
 * @author robing
 */
@RunWith(Arquillian.class)
public class EntitetsTjenesteTest {

    @Test
    public void dummy() {
        assert(true);
    }

//    @Inject
//    private EntitetsTjenesteMock tjeneste;
//
//    @Deployment
//    public static WebArchive deployment() {
//        return RESTDeployment.deployment();
//    }
//
//    @Test
//    public void getAll_ReturnererListeAvAvtaler_Liste() {
//        List<Avtale> avtaler = tjeneste.getAll(new MultivaluedHashMap<String, String>());
//        assertFalse(avtaler.isEmpty());
//    }
//
//    @Test
//    public void getCount_ReturnererAntall_IngenException() {
//        Map<String, Long> map = tjeneste.getCount(getUriInfo());
//        assertTrue(map.get("antall") > 0);
//    }
//
//    @Test
//    public void getSingleInstance_FinnerIngen_404() {
//        try {
//            tjeneste.getSingleInstance("tull");
//        } catch(EJBException e) {
//            assertEquals(NoResultException.class, e.getCause().getClass());
//        }
//    }
//
//    @Test
//    public void getSingleInstance_HenterObjekt_200() {
//        Avtale a = tjeneste.getSingleInstance("Avtale1");
//        assertTrue(a != null);
//    }
//
//    @Test
//    public void create_oppretterNy_200() {
//        Avtale rsp = tjeneste.create(getAvtale());
//        assertNotNull(rsp);
//    }
//
//    @Test
//    public void update_Oppdterer_200() {
//        Avtale a = tjeneste.getSingleInstance("Avtale1");
//        a.setAvtalebeskrivelse("ny beskrivelse");
//        Avtale rsp = tjeneste.update(a);
//        assertNotNull(rsp);
//    }
//
//    public void delete_finnerIngen_404() {
//        try {
//            tjeneste.delete("tull");
//        } catch(EJBException e) {
//            assertEquals(NoResultException.class, e.getCause().getClass());
//        }
//    }
//
//    @Test
//    public void delete_sletter_200() {
//        tjeneste.create(getAvtale());
//        Avtale rsp = tjeneste.delete(getAvtale().getAvtaleidentifikator());
//        assertNotNull(rsp);
//    }
//
//    private Avtale getAvtale() {
//        Avtale a1 = new Avtale();
//        a1.setAvtalebeskrivelse("beskrivelse");
//        a1.setAvtaleidentifikator("test-avtale");
//
//        Calendar dag = Calendar.getInstance();
//        a1.setAvtaledato(dag);
//
//        Virksomhet v = new Virksomhet();
//        v.setOrganisasjonsnummer("100");
//        v.setNavn("Testorganisasjon");
//        a1.setVirksomhet(v);
//        return a1;
//    }
    
    private UriInfo getUriInfo() {
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
