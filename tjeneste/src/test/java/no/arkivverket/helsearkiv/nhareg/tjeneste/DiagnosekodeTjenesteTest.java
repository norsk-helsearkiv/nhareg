package no.arkivverket.helsearkiv.nhareg.tjeneste;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.junit.Arquillian;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DiagnosekodeTjenesteTest {

    @Test
    public void dummy() {
        assert(true);
    }
//    @Deployment
//    public static WebArchive deployment() {
//        return RESTDeployment.deployment();
//    }
//
//    @Inject
//    private DiagnosekodeTjeneste tjeneste;
//
//    @Test
//    public void create() {
//        Diagnosekode diagnosekode = new Diagnosekode();
//        diagnosekode.setCode("Diagnosekode100");
//        diagnosekode.setCodeSystem("Kodesystem99");
//        diagnosekode.setCodeSystemVersion("0.9.Beta");
//        diagnosekode.setDisplayName("Influensa beta");
//        Diagnosekode response = tjeneste.create(diagnosekode);
//        assertNotNull(response);
//    }
//
//    @Test
//    public void getAllUtenPaginering() {
//        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
//        List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
//        assertNotNull(diagnosekoder);
//        assertEquals(3, diagnosekoder.size());
//    }
//
//    @Test
//    public void getAllMedPaginering() {
//        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
//        queryParameters.add(EntitetsTjeneste.SIDE, "1");
//        queryParameters.add(EntitetsTjeneste.ANTALL, "1");
//        List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
//        assertNotNull(diagnosekoder);
//        assertEquals(1, diagnosekoder.size());
//    }
//
//    @Test
//    public void hentDiagnosekoderMedCode() {
//        String code = "Code0";
//        List<Diagnosekode> diagnosekoder = tjeneste.hentDiagnosekoderMedCode(code);
//        assertNotNull(diagnosekoder);
//        assertEquals(1, diagnosekoder.size());
//    }
//
//    @Test
//    public void getAllMedCode() {
//        String code = "Code0";
//        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
//        queryParameters.add(DiagnosekodeTjeneste.CODE_QUERY_PARAMETER, code);
//        List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
//        assertNotNull(diagnosekoder);
//        assertEquals(1, diagnosekoder.size());
//    }
//
//    @Test
//    public void getAllDisplayNameLike() {
//        String displayNameLike = "ode";
//        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
//        queryParameters.add(DiagnosekodeTjeneste.DISPLAY_NAME_LIKE_QUERY_PARAMETER, displayNameLike);
//        List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
//        assertNotNull(diagnosekoder);
//        assertEquals(1, diagnosekoder.size());
//    }
//
//    @Test
//    public void getAllDisplayNameLikeIgnoreCase() {
//        String displayNameLike = "oDe";
//        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
//        queryParameters.add(DiagnosekodeTjeneste.DISPLAY_NAME_LIKE_QUERY_PARAMETER, displayNameLike);
//        List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
//        assertNotNull(diagnosekoder);
//        assertEquals(1, diagnosekoder.size());
//    }
//
//    @Test
//    public void hentDiagnosekoderMedUkjentCode() {
//        String code = "Ukjent";
//        List<Diagnosekode> diagnosekoder = tjeneste.hentDiagnosekoderMedCode(code);
//        assertNotNull(diagnosekoder);
//        assertEquals(0, diagnosekoder.size());
//    }
//
//    @Test
//    public void getAllMedUkjentCode() {
//        String code = "Ukjent";
//        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
//        queryParameters.add("code", code);
//        List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
//        assertNotNull(diagnosekoder);
//        assertEquals(0, diagnosekoder.size());
//    }
//
//    @Test
//    public void hentDiagnosekoderMedNulltCode() {
//        String code = null;
//        List<Diagnosekode> diagnosekoder = tjeneste.hentDiagnosekoderMedCode(code);
//        assertNotNull(diagnosekoder);
//        assertEquals(0, diagnosekoder.size());
//    }
//
//    @Test
//    public void getAllMedNulltCode() {
//        String code = null;
//        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
//        queryParameters.add("code", code);
//        List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
//        assertNotNull(diagnosekoder);
//        assertEquals(0, diagnosekoder.size());
//    }
}
