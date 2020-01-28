package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.BusinessDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class TransferServiceTest {

    private final String USERNAME = "nhabruker1";

    @Deployment
    public static WebArchive createDeployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private TransferServiceInterface transferService;

    @Inject
    private TransferConverterInterface transferConverter;
    
    @Test(expected = EntityExistsException.class)
    public void create_duplicateEntry_shouldThrowEntityExistsException() {
        final TransferDTO transferDTO = new TransferDTO();
        final AgreementDTO agreementDTO = new AgreementDTO();
        final BusinessDTO businessDTO = new BusinessDTO("100", "Testorganisasjon", null);
        final String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));

        agreementDTO.setAgreementId("test");
        agreementDTO.setAgreementDate(now);
        agreementDTO.setAgreementDescription("Test");
        agreementDTO.setBusiness(businessDTO);
        transferDTO.setTransferId("Avlevering-1");
        transferDTO.setTransferDescription("test");
        transferDTO.setAgreement(agreementDTO);

        transferService.create(transferDTO, USERNAME);
    }

    @Test
    public void getById_getValidId_shouldReturnTransfer() {
        final TransferDTO transferDTO = transferService.getById("Avlevering-1");

        assertNotNull(transferDTO);
        assertNotNull(transferDTO.getAgreement());
        assertNotNull(transferDTO.getUpdateInfo());
    }

    @Test(expected = ValidationErrorException.class)
    public void delete_hasMedicalRecords_shouldThrowValidationException() {
        transferService.delete("Avlevering-1");
    }

    @Test
    public void update_updateArchiveCreator_shouldReturnUpdated() {
        final String id = "Avlevering-1";
        final String archiveCreator = "JUnit test";
        final TransferDTO transferDTO = transferService.getById(id);
        final Transfer transfer = transferConverter.toTransfer(transferDTO, null);

        assertNotNull(transfer);

        transferDTO.setArchiveCreator(archiveCreator);

        final TransferDTO updateDTO = transferConverter.fromTransfer(transfer);
        transferService.update(updateDTO, USERNAME);

        final TransferDTO updatedTransferDTO = transferService.getById(id);
        assertNotNull(updatedTransferDTO);
        assertNotNull(updatedTransferDTO.getAgreement());
        assertNotNull(updatedTransferDTO.getUpdateInfo());
    }

    @Test
    public void getTransferForStorageUnit_validId_shouldReturnTransfer() {
        final String storageId = "boks3";
        final TransferDTO transferDTO = transferService.getTransferForStorageUnit(storageId);

        assertNotNull(transferDTO);
        assertEquals("Avlevering-2", transferDTO.getTransferId());
    }
    
    @Test
    public void getDefaultTransfer_noDefault_shouldReturnNull() {
        final TransferDTO transferDTO = transferService.getDefaultTransfer(USERNAME);
        assertNull(transferDTO);
    }

    @Test
    public void lockTransfer_setLocked_shouldReturnLocked() {
        final String id = "Avlevering-1";
        final TransferDTO transferDTO = transferService.getById(id);
        assertFalse(transferDTO.isLocked());

        final TransferDTO lockedDTO = transferService.lockTransfer(id);
        assertTrue(lockedDTO.isLocked());
    }

    @Test
    public void unlockTransfer_setUnlocked_shouldReturnUnlocked() {
        final String id = "Avlevering-1";
        final TransferDTO transferDTO = transferService.getById(id);

        assertFalse(transferDTO.isLocked());

        final TransferDTO lockedDTO = transferService.lockTransfer(id);
        assertTrue(lockedDTO.isLocked());

        final TransferDTO unlockedDTO = transferService.unlockTransfer(id);
        assertFalse(unlockedDTO.isLocked());
    }
}