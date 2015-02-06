package no.arkivverket.helsearkiv.nhareg.tjeneste;


import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class PasientjournalTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private PasientjournalTjeneste tjeneste;
    
    @Test
    public void testGet() {
        UriInfo info = getInfo();
        Response rsp = tjeneste.hentPasientjournaler(info);
        ListeObjekt lst = (ListeObjekt) rsp.getEntity();
        assertTrue(lst.getAntall() > 0);
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

    /*
    PasientjournalTjeneste har kun funksjonalitet for GET, PUT og DELETE.
    For POST, se AvleveringTjeneste/{id}/pasientjournaler
    
    @Test
    public void testCreate(){
        Pasientjournal a = new Pasientjournal();
        tjeneste.create(a);
    }

    @Test
    public void testCreateMedKjønn() {
        Pasientjournal pasientjournal = new Pasientjournal();
        Grunnopplysninger grunnopplysninger = new Grunnopplysninger();
        Kjønn kjønn = new Kjønn();
        kjønn.setCode("M");
        grunnopplysninger.setKjønn(kjønn);
        pasientjournal.setGrunnopplysninger(grunnopplysninger);
        tjeneste.create(pasientjournal);
    }

    @Test(expected = javax.ejb.EJBTransactionRolledbackException.class)
    public void testCreateMedUgyldigKjønn() {
        Pasientjournal pasientjournal = new Pasientjournal();
        Grunnopplysninger grunnopplysninger = new Grunnopplysninger();
        Kjønn kjønn = new Kjønn();
        kjønn.setCode("tull");
        grunnopplysninger.setKjønn(kjønn);
        pasientjournal.setGrunnopplysninger(grunnopplysninger);
        tjeneste.create(pasientjournal);
    }*/
}
