package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.agreement.AgreementConverterInterface;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferInAgreementDTO;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;

import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class TransferServiceTest {
    
    @Deployment
    public static WebArchive createDeployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private TransferServiceInterface transferService;
    
    @Inject
    private TransferConverterInterface transferConverter;
    
    @Inject
    private AgreementConverterInterface agreementConverter;
    
    @Test(expected = EntityExistsException.class)
    public void create_duplicateEntry_shouldThrowEntityExistsException() {
        final Transfer transfer = new Transfer();
        transfer.setTransferId("Avlevering-1");
        final TransferDTO transferDTO = transferConverter.fromTransfer(transfer);
        
        transferService.create(transferDTO, "nhabruker1");
    }
    
    @Test
    public void getById_getValidId_shouldReturnTransfer() {
        final TransferDTO transferDTO = transferService.getById("Avlevering-1");
        final Transfer transfer = transferConverter.toTransfer(transferDTO);

        assertNotNull(transfer);
        assertNotNull(transfer.getMedicalRecords());
        assertNotNull(transfer.getAgreement());
        assertNotNull(transfer.getUpdateInfo());
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
        final TransferDTO transferDTO = transferService.getById(id);
        final Transfer transfer = transferConverter.toTransfer(transferDTO);
        final AgreementDTO agreementDTO = agreementConverter.fromAgreement(transfer.getAgreement());
        
        assertNotNull(transfer);
        assertNotNull(agreementDTO);
        assertNotNull(transfer.getMedicalRecords());

        transferDTO.setArchiveCreator(archiveCreator);
        final TransferInAgreementDTO transferInAgreementDTO = transferConverter.toInAgreementDTO(transfer,
                                                                                                 new Business(),
                                                                                                 agreementDTO);
        transferService.update(transferInAgreementDTO,"nhabruker1");

        final TransferDTO updatedTransferDTO = transferService.getById(id);
        final Transfer updatedTransfer = transferConverter.toTransfer(updatedTransferDTO);
        assertNotNull(updatedTransfer);
        assertNotNull(updatedTransfer.getAgreement());
        assertNotNull(updatedTransfer.getUpdateInfo());
    }
    
    @Test
    public void getTransferForStorageUnit_validId_shouldReturnTransfer() {
        final String storageId = "boks1";

        final TransferDTO transferDTO = transferService.getTransferForStorageUnit(storageId);
        final Transfer transfer = transferConverter.toTransfer(transferDTO);
        assertNotNull(transfer);
        assertNotNull(transfer.getMedicalRecords());
    }
    
}