package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
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
public class TransferServiceTest {
    
    @Deployment
    public static WebArchive createDeployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private TransferServiceInterface transferService;
    
    @Test
    public void getById_getValidId_shouldReturnTransfer() {
        final Avlevering transfer = transferService.getById("Avlevering-1");
        assertNotNull(transfer);
        assertNotNull(transfer.getPasientjournal());
        assertNotNull(transfer.getAvtale());
        assertNotNull(transfer.getOppdateringsinfo());
    }
    
    @Test(expected = ValideringsfeilException.class)
    public void delete_hasMedicalRecords_shouldThrowValidationException() {
        transferService.delete("Avlevering-1");
    }
    
    @Test
    public void update_updateArchiveCreator_shouldReturnUpdated() {
        final String id = "Avlevering-1";
        final String archiveCreator = "JUnit test";
        final Avlevering transfer = transferService.getById(id);
        assertNotNull(transfer);
        assertNotNull(transfer.getAvtale());
        assertNotNull(transfer.getPasientjournal());

        final TransferDTO transferDTO = new TransferDTO(transfer);
        transferDTO.setArkivskaper(archiveCreator);
        transferService.update(transferDTO, "nhabruker1");
        
        final Avlevering updatedTransfer = transferService.getById(id);
        assertNotNull(updatedTransfer);
        assertNotNull(updatedTransfer.getAvtale());
        assertNotNull(updatedTransfer.getOppdateringsinfo());
        assertEquals(archiveCreator, updatedTransfer.getArkivskaper());
    }
    
    @Test
    public void getTransferForStorageUnit_validId_shouldReturnTransfer() {
        final String storageId = "enhet-1";
        
        final Avlevering transfer = transferService.getTransferForStorageUnit(storageId);
        assertNotNull(transfer);
        assertNotNull(transfer.getPasientjournal());
    }
}