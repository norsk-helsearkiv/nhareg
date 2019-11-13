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

@RunWith(Arquillian.class)
public class PasientjournalTjenesteTest {

    @Test
    public void dummy() {
        assert(true);
    }
//    @Inject
//    private PasientjournalTjeneste tjeneste;
//
//    @Deployment
//    public static WebArchive deployment() {
//        return RESTDeployment.deployment();
//    }
//
//    // GET
//    @Test
//    public void getAll_utenQueryParameter_allePasientjournaler() {
//        ListeObjekt listeObjekt = tjeneste.hentAlle(getUriInfoTom());
//        assertEquals(2, listeObjekt.getTotal());
//    }
//
//    @Test
//    public void getActiveWithPaging_henterEnFraSideTo_andreElementIListen() {
//        //Henter alle pasientjournaler i databasen for test av paging
//        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<String, String>();
//        List<Pasientjournal> pasientjournaler = tjeneste.getAll(map);
//
//        UriInfo info = getUriInfo();
//        ListeObjekt listeObjekt = tjeneste.getActiveWithPaging(pasientjournaler, info);
//
//        assertEquals(2, listeObjekt.getTotal());
//        assertEquals(1, listeObjekt.getAntall());
//        List<PasientjournalSokeresultatDTO> resultatListe
//                = (List<PasientjournalSokeresultatDTO>) listeObjekt.getListe();
//        assertEquals("uuid3", resultatListe.get(0).getUuid());
//    }
//
//    @Test
//    public void getSingleInstance_henterForsteObjekt_returnererDTO() {
//        PasientjournalDTO dto = tjeneste.getPasientjournalDTO("uuid1");
//
//        assertEquals("Hunden Fido", dto.getPersondata().getNavn());
//        assertEquals(3, dto.getPersondata().getLagringsenheter().length);
//        assertFalse(dto.getDiagnoser().isEmpty());
//    }
//
//    // POST
//    @Test
//    public void leggTilDiagnose() {
//        Pasientjournal pasientjournal = tjeneste.hent("uuid1");
//        assertNotNull(pasientjournal);
//        assertNotNull(pasientjournal.getDiagnose());
//        //
//        // Henter antall diagnoser før.
//        //
//        int antallDiagnoserFør = pasientjournal.getDiagnose().size();
//        //
//        // Henter antall lagringsenheter før.
//        //
//        assertNotNull(pasientjournal.getLagringsenhet());
//        int antallLagringsenheterFør = pasientjournal.getLagringsenhet().size();
//        //
//        // Legger til diagnose.
//        //
//        DiagnoseDTO dto = new DiagnoseDTO();
//        dto.setDiagnosedato("15.01.2015");
//        dto.setDiagnosetekst("Jeg er syk");
//        dto.setDiagnosekode("Code0");
//        Response response = tjeneste.leggTilDiagnose("uuid1", dto);
//        assertNotNull(response);
//        //
//        // Sjekker at antall diagnoser har økt med 1.
//        //
//        pasientjournal = tjeneste.hent("uuid1");
//        assertNotNull(pasientjournal);
//        assertNotNull(pasientjournal.getDiagnose());
//        assertEquals(antallDiagnoserFør + 1, pasientjournal.getDiagnose().size());
//        //
//        // Sjekker at antall lagringsenheter er det samme
//        //
//        assertNotNull(pasientjournal.getLagringsenhet());
//        assertEquals(antallLagringsenheterFør, pasientjournal.getLagringsenhet().size());
//
//    }
//
//    @Test
//    public void fjernDiagnose() {
///*
//        Pasientjournal pj = tjeneste.hent("uuid1");
//        assertNotNull(pj);
//        assertNotNull(pj.getDiagnose());
//        assertFalse(pj.getDiagnose().isEmpty());
//        int sizeFor = pj.getDiagnose().size();
//
//        DiagnoseDTO dto = new DiagnoseDTO();
//        dto.setUuid(pj.getDiagnose().get(0).getUuid());
//        Response response = tjeneste.fjernDiagnose("uuid1", dto);
//        assertNotNull(response);
//        assertEquals(200, response.getStatus());
//        //
//        pj = tjeneste.hent("uuid1");
//        assertNotNull(pj);
//        assertNotNull(pj.getDiagnose());
//        assertEquals(sizeFor - 1, pj.getDiagnose().size());
//*/
//    }
//
////    @Test
//    public void leggTilDiagnoseNull() {
//        Response response = tjeneste.leggTilDiagnose("uuid1", null);
//        assertNotNull(response);
//    }
//
//    // PUT
//    @Test
//    public void oppdaterPasientjournal_setterNyttJournalnummer_ok() throws ParseException {
//        PasientjournalDTO pasientjournalDTO = tjeneste.getPasientjournalDTO("uuid1");
//        pasientjournalDTO.getPersondata().setJournalnummer("12345");
//        tjeneste.oppdaterPasientjournal(pasientjournalDTO);
//        //Ingen feilmeldinger
//    }
//
//    @Test
//    public void oppdaterPasientjournal_antall_diagnoser() throws ParseException {
//        PasientjournalDTO pasientjournalDTO = tjeneste.getPasientjournalDTO("uuid1");
//        assertNotNull(pasientjournalDTO);
//        assertNotNull(pasientjournalDTO.getDiagnoser());
//        assertFalse(pasientjournalDTO.getDiagnoser().isEmpty());
//        int antallDiagnoser = pasientjournalDTO.getDiagnoser().size();
//        //
//        tjeneste.oppdaterPasientjournal(pasientjournalDTO);
//        //
//        // Sjekker antall diagnoser som er lagret
//        //
//        pasientjournalDTO = tjeneste.getPasientjournalDTO("uuid1");
//        assertNotNull(pasientjournalDTO);
//        assertNotNull(pasientjournalDTO.getDiagnoser());
//        assertEquals(antallDiagnoser, pasientjournalDTO.getDiagnoser().size());
//    }
//
//    @Test
//    public void oppdaterPasientjournal_antall_Lagringsenheter() throws ParseException {
//        String id = "uuid1";
//        Pasientjournal pasientjournal = tjeneste.hent(id);
//        assertNotNull(pasientjournal);
//        assertNotNull(pasientjournal.getLagringsenhet());
//        assertEquals(3, pasientjournal.getLagringsenhet().size());
//        PasientjournalDTO pasientjournalDTO = tjeneste.getPasientjournalDTO("uuid1");
//        assertNotNull(pasientjournalDTO);
//        //
//        //
//        assertNotNull(pasientjournalDTO.getPersondata());
//        assertNotNull(pasientjournalDTO.getPersondata().getLagringsenheter());
//        assertEquals(3, pasientjournalDTO.getPersondata().getLagringsenheter().length);
//        //
//        // Gjør en oppdatering.
//        //
//        tjeneste.oppdaterPasientjournal(pasientjournalDTO);
//        //
//        // Sjekker antall diagnoser som er lagret
//        //
//        pasientjournalDTO = tjeneste.getPasientjournalDTO("uuid1");
//        assertNotNull(pasientjournalDTO);
//        assertNotNull(pasientjournalDTO.getPersondata());
//        assertNotNull(pasientjournalDTO.getPersondata().getLagringsenheter());
//        assertEquals(3, pasientjournalDTO.getPersondata().getLagringsenheter().length);
//    }
//
//    @Test
//    public void oppdaterPasientjournal_nha_98() throws ParseException {
//        //
//        // Oppretter diagnoser uten diagnosekoder, men med tekst og lagrer.
//        // Tar opp igjen pasientjournalen og endrer f.eks. dato.
//        // Resultatet er at de registrerte diagnosetekstene er forsvunnet.
//        //
//        String id = "uuid1";
//        PasientjournalDTO pasientjournalDTO = tjeneste.getPasientjournalDTO(id);
//        assertNotNull(pasientjournalDTO);
//        //
//        // Legger til diagnose.
//        //
//        DiagnoseDTO dto = new DiagnoseDTO();
//        dto.setDiagnosedato("15.01.2015");
//        dto.setDiagnosetekst("Jeg er syk");
//        Response response = tjeneste.leggTilDiagnose("uuid1", dto);
//        assertNotNull(response);
//
//        pasientjournalDTO = tjeneste.getPasientjournalDTO(id);
//        assertNotNull(pasientjournalDTO);
//        pasientjournalDTO.getPersondata().setFodt("1990");
//        tjeneste.oppdaterPasientjournal(pasientjournalDTO);
//    }
//
//    // DELETE
//    @Test
//    public void delete_finnerIkkeEntitet_404() {
//        try {
//            tjeneste.delete("tull");
//        } catch (EJBException e) {
//            assertEquals(NoResultException.class, e.getCause().getClass());
//        }
//    }
//
//    @Test
//    public void delete_sletterEntitet_200() {
//        Pasientjournal rsp = tjeneste.delete("uuid1");
//        assertNotNull(rsp);
//    }

    private UriInfo getUriInfoTom() {
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
                MultivaluedHashMap<String, String> map = new MultivaluedHashMap<String, String>();
                map.add("side", "2");
                map.add("antall", "1");
                return map;
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
