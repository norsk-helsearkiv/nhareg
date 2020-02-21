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
    public void create_validRecord_shouldReturnRecord() {
        final MedicalRecordDTO medicalRecordDTO = generateDTO();
        
        final MedicalRecordDTO createdDTO = medicalRecordService.create(medicalRecordDTO, USERNAME);
        
        assertNotNull(createdDTO);
    }

    @Test(expected = ValidationErrorException.class)
    public void create_invalidPID_shouldThrowValidationError() {
        final MedicalRecordDTO recordDTO = generateDTO();
        recordDTO.setPid("invalid");
        
        medicalRecordService.create(recordDTO, USERNAME);        
    }
    
    @Test(expected = ValidationErrorException.class)
    public void create_bornAfterDead_shouldThrowValidationError() {
        final MedicalRecordDTO recordDTO = generateDTO();
        recordDTO.setBorn("01.01.2000");
        recordDTO.setDead("1950");
        
        medicalRecordService.create(recordDTO, USERNAME);
    }
    
    @Test(expected = ValidationErrorException.class)
    public void create_bornBeforeLowLim_shouldThrowValidationError() {
        final MedicalRecordDTO recordDTO = generateDTO();
        recordDTO.setBorn("01.01.1790");
        recordDTO.setDead("1850");
        
        medicalRecordService.create(recordDTO, USERNAME);
    }

    @Test(expected = ValidationErrorException.class)
    public void create_deadBeforeLowLim_shouldThrowValidationError() {
        final MedicalRecordDTO recordDTO = generateDTO();
        recordDTO.setBorn("ukjent");
        recordDTO.setDead("1790");

        medicalRecordService.create(recordDTO, USERNAME);
    }
    
    @Test(expected = ValidationErrorException.class)
    public void create_deadWithinWaitLim_shouldThrowValidationError() {
        final MedicalRecordDTO recordDTO = generateDTO();
        recordDTO.setBorn("01.01.1970");
        recordDTO.setDead("2020");
        
        medicalRecordService.create(recordDTO, USERNAME);
    }
    
    @Test(expected = ValidationErrorException.class)
    public void create_aboveMaxAge_shouldThrowValidationError() {
        final MedicalRecordDTO recordDTO = generateDTO();
        recordDTO.setBorn("01.01.1850");
        recordDTO.setDead("2000");

        medicalRecordService.create(recordDTO, USERNAME);
    }

    @Test(expected = ValidationErrorException.class)
    public void create_firstContactBeforeBorn_shouldThrowValidationError() {
        final MedicalRecordDTO recordDTO = generateDTO();
        recordDTO.setFirstContact("01.01.1940");
        
        medicalRecordService.create(recordDTO, USERNAME);
    }

    @Test(expected = ValidationErrorException.class)
    public void create_firstContactAfterDead_shouldThrowValidationError() {
        final MedicalRecordDTO recordDTO = generateDTO();
        recordDTO.setLastContact("01.01.2001");

        medicalRecordService.create(recordDTO, USERNAME);
    }

    @Test(expected = ValidationErrorException.class)
    public void create_lastContactBeforeBorn_shouldThrowValidationError() {
        final MedicalRecordDTO recordDTO = generateDTO();
        recordDTO.setLastContact("01.01.1900");
        
        medicalRecordService.create(recordDTO, USERNAME);
    }

    @Test(expected = ValidationErrorException.class)
    public void create_lastContactAfterDead_shouldThrowValidationError() {
        final MedicalRecordDTO recordDTO = generateDTO();
        recordDTO.setLastContact("01.01.2001");
        
        medicalRecordService.create(recordDTO, USERNAME);
    }

    @Test(expected = ValidationErrorException.class)
    public void create_firstContactAfterLastContact_shouldThrowValidationError() {
        final MedicalRecordDTO recordDTO = generateDTO();
        recordDTO.setFirstContact("01.01.1970");
        recordDTO.setLastContact("01.01.1960");

        medicalRecordService.create(recordDTO, USERNAME);
    }
    
    @Test
    public void getByIdWithTransfer_shouldReturnThreeStorageUnits() {
        final MedicalRecordDTO medicalRecordDTO = medicalRecordService.getByIdWithTransfer("uuid1");
        assertEquals("Hunden Fido", medicalRecordDTO.getName());
        assertEquals(3, medicalRecordDTO.getStorageUnits().length);
    }

    @Test
    public void updateMedicalRecord_newRecordNumber() {
        MedicalRecordDTO medicalRecordDTO = medicalRecordService.getByIdWithTransfer("uuid3");
        medicalRecordDTO.setRecordNumber("12345");
        final MedicalRecordDTO updated = medicalRecordService.update(medicalRecordDTO, USERNAME);

        assertNotNull(updated);
        assertEquals("12345", updated.getRecordNumber());
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
        final MedicalRecordDTO deletedDTO = medicalRecordService.delete("uuid3", USERNAME);

        assertNotNull(deletedDTO);
        assertEquals(true, deletedDTO.getDeleted());
    }

    private MedicalRecordDTO generateDTO() {
        final String[] storageUnits = new String[] { "testboks" };
        return MedicalRecordDTO.builder()
                               .uuid("test-generated")
                               .name("test")
                               .storageUnits(storageUnits)
                               .fanearkid(635371878549L)
                               .pid("01030182134")
                               .gender("M")
                               .born("01.01.1945")
                               .dead("01.01.2000")
                               .transferId("Avlevering-1")
                               .build();
    }

}