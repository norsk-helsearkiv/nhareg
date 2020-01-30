package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class MedicalRecordServiceTest {

    private static final String USERNAME = "nhabruker1";
    
    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private MedicalRecordServiceInterface medicalRecordService;

    @Test
    public void getByIdWithTransfer_shouldReturnThreeStorageUnits() {
        final MedicalRecordDTO medicalRecordDTO = medicalRecordService.getByIdWithTransfer("uuid1");
        assertEquals("Hunden Fido", medicalRecordDTO.getName());
        assertEquals(3, medicalRecordDTO.getStorageUnits().length);
    }

    @Test
    public void updateMedicalRecord_newJournalNumber() {
        MedicalRecordDTO medicalRecordDTO = medicalRecordService.getByIdWithTransfer("uuid1");
        medicalRecordDTO.setRecordNumber("12345");
        medicalRecordService.update(medicalRecordDTO, USERNAME);
    }

    @Test
    public void updateMedicalRecord_shouldNotChangeStorageUnits() {
        final String id = "uuid1";
        final MedicalRecordDTO recordDTO = medicalRecordService.getById(id);
        assertNotNull(recordDTO);
        assertNotNull(recordDTO.getStorageUnits());
        assertEquals(3, recordDTO.getStorageUnits().length);
        assertEquals(2, recordDTO.getDiagnoses().size());
        
        MedicalRecordDTO medicalRecordDTO = medicalRecordService.getByIdWithTransfer(id);
        assertNotNull(medicalRecordDTO);
        assertNotNull(medicalRecordDTO.getStorageUnits());
        assertEquals(3, medicalRecordDTO.getStorageUnits().length);

        // Do an update
        medicalRecordService.update(medicalRecordDTO, USERNAME);

        // Checks the number of diagnosis that are saved
        medicalRecordDTO = medicalRecordService.getByIdWithTransfer(id);
        assertNotNull(medicalRecordDTO);
        assertNotNull(medicalRecordDTO.getStorageUnits());
        assertEquals(3, medicalRecordDTO.getStorageUnits().length);
    }

    @Test(expected = ValidationErrorException.class)
    public void delete_invalidId_shouldThrowValidationErrorException() {
        medicalRecordService.delete("tull", USERNAME);
    }

    @Test
    public void delete_validId_shouldSetDeletedToTrue() {
        final MedicalRecordDTO recordDTO = medicalRecordService.delete("uuid1", USERNAME);
        assertNotNull(recordDTO);
        assertEquals(true, recordDTO.getDeleted());
    }
    
}