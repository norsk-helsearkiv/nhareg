package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.diagnosiscode.DiagnosisCodeServiceInterface;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;
import org.junit.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DiagnosekodeServiceTest {

    @Inject
    private DiagnosisCodeServiceInterface diagnosisCodeService;

    @Test
    public void create_shouldNotReturnNull() {
        final Diagnosekode diagnosisCode = new Diagnosekode();
        diagnosisCode.setCode("Diagnosekode100");
        diagnosisCode.setCodeSystem("Kodesystem99");
        diagnosisCode.setCodeSystemVersion("0.9.Beta");
        diagnosisCode.setDisplayName("Influensa beta");
        Diagnosekode response = diagnosisCodeService.create(diagnosisCode);
        assertNotNull(response);
    }

    @Test
    public void getAll_noPaging_shouldFindThree() {
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
        List<Diagnosekode> diagnosisCodes = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(diagnosisCodes);
        assertEquals(2, diagnosisCodes.size());
    }

    @Test
    public void getAll_withPaging_shouldFindOne() {
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
        queryParameters.add(EntitetsTjeneste.SIDE, "1");
        queryParameters.add(EntitetsTjeneste.ANTALL, "1");
        List<Diagnosekode> diagnosisCodes = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(diagnosisCodes);
        assertEquals(1, diagnosisCodes.size());
    }

    @Test
    public void getAll_withCode_shouldReturnOne() {
        final String code = "Code0";
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
        queryParameters.add("code", code);
        final List<Diagnosekode> diagnosisCodes = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(diagnosisCodes);
        assertEquals(1, diagnosisCodes.size());
    }

    @Test
    public void getAll_odeParam_shouldReturnOne() {
        final String displayNameLike = "ode";
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("displayNameLike", displayNameLike);
        final List<Diagnosekode> diagnosisCodes = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(diagnosisCodes);
        assertEquals(1, diagnosisCodes.size());
    }

    @Test
    public void getAll_displayNameParam_ignoreCase_shouldReturnOne() {
        final String displayNameLike = "oDe";
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
        queryParameters.add("displayNameLike", displayNameLike);
        final List<Diagnosekode> diagnosisCodes = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(diagnosisCodes);
        assertEquals(1, diagnosisCodes.size());
    }

    @Test
    public void getAll_codeParam_unknownCode_shouldReturnNone() {
        final String code = "Ukjent";
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("code", code);
        final List<Diagnosekode> diagnosisCodes = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(diagnosisCodes);
        assertEquals(0, diagnosisCodes.size());
    }

    @Test
    public void getAll_nullCode_shouldReturnNone() {
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
        queryParameters.add("code", null);
        final List<Diagnosekode> diagnosisCodes = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(diagnosisCodes);
        assertEquals(0, diagnosisCodes.size());
    }
}
