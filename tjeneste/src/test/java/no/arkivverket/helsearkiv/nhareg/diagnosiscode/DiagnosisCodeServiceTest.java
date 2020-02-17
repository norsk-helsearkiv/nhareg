package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisCodeDTO;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class DiagnosisCodeServiceTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private DiagnosisCodeServiceInterface diagnosisCodeService;

    @Test
    public void create_shouldNotReturnNull() {
        final DiagnosisCodeDTO codeDTO = new DiagnosisCodeDTO();
        codeDTO.setCode("Diagnosekode100");
        codeDTO.setCodeSystem("Kodesystem99");
        codeDTO.setCodeSystemVersion("0.9.Beta");
        codeDTO.setDisplayName("Influensa beta");
        
        final DiagnosisCodeDTO response = diagnosisCodeService.create(codeDTO);
        assertNotNull(response);
    }
    
    @Test
    public void delete_validDiagnosis() {
        final String id = "Code0";
        final DiagnosisCodeDTO response = diagnosisCodeService.delete(id);
        
        assertNotNull(response);
    }
    
    @Test
    public void getAll_noPaging_shouldReturnThreeOrMore() {
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        final List<DiagnosisCodeDTO> codeDTOList = diagnosisCodeService.getAll(queryParameters);
     
        assertNotNull(codeDTOList);
        assertTrue(codeDTOList.size() >= 3);
    }

    @Test
    public void getAll_withPaging_shouldReturnOne() {
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("page", "1");
        queryParameters.add("size", "1");

        final List<DiagnosisCodeDTO> codeDTOList = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(codeDTOList);
        assertEquals(1, codeDTOList.size());
    }

    @Test
    public void getAll_withCode_shouldReturnOne() {
        final String code = "Code0";
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("code", code);

        final List<DiagnosisCodeDTO> diagnosisCodes = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(diagnosisCodes);
        assertEquals(1, diagnosisCodes.size());
    }

    @Test
    public void getAll_odeParam_shouldReturnOne() {
        final String displayNameLike = "ode";
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("name", displayNameLike);
        
        final List<DiagnosisCodeDTO> codeDTOList = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(codeDTOList);
        assertEquals(3, codeDTOList.size());
    }

    @Test
    public void getAll_displayNameParam_ignoreCase_shouldReturnThree() {
        final String displayNameLike = "oDe";
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("name", displayNameLike);
        
        final List<DiagnosisCodeDTO> codeDTOList = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(codeDTOList);
        assertEquals(3, codeDTOList.size());
    }

    @Test
    public void getAll_codeParam_unknownCode_shouldReturnNone() {
        final String code = "Ukjent";
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("code", code);
        
        final List<DiagnosisCodeDTO> codeDTOList = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(codeDTOList);
        assertEquals(0, codeDTOList.size());
    }

    @Test
    public void getAll_nullCode_shouldReturnThreeOrMore() {
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("code", null);
        
        final List<DiagnosisCodeDTO> codeDTOList = diagnosisCodeService.getAll(queryParameters);
        assertNotNull(codeDTOList);
        assertTrue(codeDTOList.size() >= 3);
    }
    
    @Test
    public void getAll_matchingAll_shouldReturnThreeOrMore() {
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.put("code", Collections.singletonList("Code"));
        final List<DiagnosisCodeDTO> codeDTOList = diagnosisCodeService.getAll(queryParameters);
        
        assertNotNull(codeDTOList);
        assertTrue(codeDTOList.size() >= 3);
    }

    @Test
    public void getAll_matchingOne_shouldReturnOne() {
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.put("code", Collections.singletonList("Code1"));
        final List<DiagnosisCodeDTO> codeDTOList = diagnosisCodeService.getAll(queryParameters);

        assertNotNull(codeDTOList);
        assertEquals(1, codeDTOList.size());
    }

    @Test
    public void getAll_matchingNone_shouldReturnNone() {
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.put("code", Collections.singletonList("NoCode"));
        final List<DiagnosisCodeDTO> codeDTOList = diagnosisCodeService.getAll(queryParameters);

        assertNotNull(codeDTOList);
        assertEquals(0, codeDTOList.size());
    }
    
    @Test
    public void getAll_matchingAll_sizeOne_shouldReturnOne() {
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.put("code", Collections.singletonList("Code"));
        queryParameters.put("page", Collections.singletonList("1"));
        queryParameters.put("size", Collections.singletonList("1"));
        
        final List<DiagnosisCodeDTO> codeDTOList = diagnosisCodeService.getAll(queryParameters);

        assertNotNull(codeDTOList);
        assertEquals(1, codeDTOList.size());
    }

}