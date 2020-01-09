package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class TransferServiceTest {
    
    @Deployment
    public static WebArchive createDeployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private TransferServiceInterface transferService;
    
    @Test(expected = EntityExistsException.class)
    public void create_duplicateEntry_shouldThrowEntityExistsException() {
        final Transfer transfer = new Transfer();
        transfer.setTransferId("Avlevering-1");
        final TransferDTO transferDTO = new TransferDTO(transfer);
        
        transferService.create(transferDTO, "nhabruker1");
    }
    
    @Test
    public void getById_getValidId_shouldReturnTransfer() {
        final Transfer transfer = transferService.getById("Avlevering-1").toTransfer();
        assertNotNull(transfer);
        assertNotNull(transfer.getMedicalRecords());
        assertNotNull(transfer.getAgreement());
        assertNotNull(transfer.getOppdateringsinfo());
        transfer.getMedicalRecords().forEach(medicalRecord -> {
            assertNotNull(medicalRecord.getDiagnosis()); 
            assertNotNull(medicalRecord.getStorageUnit());
        });
    }
    
    @Test(expected = ValidationErrorException.class)
    public void delete_hasMedicalRecords_shouldThrowValidationException() {
        transferService.delete("Avlevering-1");
    }
    
    @Test
    public void update_updateArchiveCreator_shouldReturnUpdated() {
        final String id = "Avlevering-1";
        final String archiveCreator = "JUnit test";
        final Transfer transfer = transferService.getById(id).toTransfer();
        assertNotNull(transfer);
        assertNotNull(transfer.getAgreement());
        assertNotNull(transfer.getMedicalRecords());

        final TransferDTO transferDTO = new TransferDTO(transfer);
        transferDTO.setArchiveCreator(archiveCreator);
        transferService.update(transferDTO, "nhabruker1");
        
        final Transfer updatedTransfer = transferService.getById(id).toTransfer();
        assertNotNull(updatedTransfer);
        assertNotNull(updatedTransfer.getAgreement());
        assertNotNull(updatedTransfer.getOppdateringsinfo());
        assertEquals(archiveCreator, updatedTransfer.getArkivskaper());
    }
    
    @Test
    public void getTransferForStorageUnit_validId_shouldReturnTransfer() {
        final String storageId = "boks1";
        
        final Transfer transfer = transferService.getTransferForStorageUnit(storageId).toTransfer();
        assertNotNull(transfer);
        assertNotNull(transfer.getMedicalRecords());
    }
    
}