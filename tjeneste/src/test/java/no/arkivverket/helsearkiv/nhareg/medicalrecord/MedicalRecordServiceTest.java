package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class MedicalRecordServiceTest {

    private static String USERNAME = "nhabruker1";
    
    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private MedicalRecordServiceInterface medicalRecordService;

    @Test
    public void getByIdWithTransfer_shouldReturnThreeStorageUnits() {
        final MedicalRecordDTO medicalRecordDTO = medicalRecordService.getByIdWithTransfer("uuid1");
        assertEquals("Hunden Fido", medicalRecordDTO.getPersondata().getNavn());
        assertEquals(3, medicalRecordDTO.getPersondata().getLagringsenheter().length);
        assertFalse(medicalRecordDTO.getDiagnoser().isEmpty());
    }

    @Test
    public void updateMedicalRecord_newJournalNumber() {
        MedicalRecordDTO medicalRecordDTO = medicalRecordService.getByIdWithTransfer("uuid1");
        medicalRecordDTO.getPersondata().setJournalnummer("12345");
        medicalRecordService.updateMedicalRecord(medicalRecordDTO, USERNAME);
    }

    @Test
    public void updateMedicalRecord_newDescription() {
        final MedicalRecordDTO medicalRecordDTO = medicalRecordService.getByIdWithTransfer("uuid1");
        assertNotNull(medicalRecordDTO);

        final String beskrivelse = "ny beskrivelse";
        medicalRecordDTO.setAvleveringBeskrivelse(beskrivelse);
        medicalRecordService.updateMedicalRecord(medicalRecordDTO, USERNAME);

        final MedicalRecordDTO updatedMedicalRecordDTO = medicalRecordService.getByIdWithTransfer("uuid1");
        assertNotNull(updatedMedicalRecordDTO);
        assertEquals(beskrivelse, updatedMedicalRecordDTO.getAvleveringBeskrivelse());
    }

    @Test
    public void updateMedicalRecord_shouldNotChangeStorageUnits() {
        final String id = "uuid1";
        final Pasientjournal medicalRecord = medicalRecordService.getById(id);
        assertNotNull(medicalRecord);
        assertNotNull(medicalRecord.getLagringsenhet());
        assertEquals(3, medicalRecord.getLagringsenhet().size());

        MedicalRecordDTO medicalRecordDTO = medicalRecordService.getByIdWithTransfer(id);
        assertNotNull(medicalRecordDTO);
        assertNotNull(medicalRecordDTO.getPersondata());
        assertNotNull(medicalRecordDTO.getPersondata().getLagringsenheter());
        assertEquals(3, medicalRecordDTO.getPersondata().getLagringsenheter().length);

        // Do an update
        medicalRecordService.updateMedicalRecord(medicalRecordDTO, USERNAME);

        // Checks the number of diagnosis that are saved
        medicalRecordDTO = medicalRecordService.getByIdWithTransfer(id);
        assertNotNull(medicalRecordDTO);
        assertNotNull(medicalRecordDTO.getPersondata());
        assertNotNull(medicalRecordDTO.getPersondata().getLagringsenheter());
        assertEquals(3, medicalRecordDTO.getPersondata().getLagringsenheter().length);
    }

    @Test
    public void delete_invalidId_shouldThrowNoResultException() {
        try {
            medicalRecordService.delete("tull", USERNAME);
        } catch (EJBException ejb) {
            assertEquals(ejb.getCause().getClass(), NoResultException.class);
        }
    }

    @Test
    public void delete_validId_shouldSetDeletedToTrue() {
        Pasientjournal medicalRecord = medicalRecordService.delete("uuid1", USERNAME);
        assertNotNull(medicalRecord);
        assertEquals(true, medicalRecord.getSlettet());
    }
    
}