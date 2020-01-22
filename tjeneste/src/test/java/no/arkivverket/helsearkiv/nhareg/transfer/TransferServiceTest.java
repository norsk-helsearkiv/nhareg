package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.agreement.AgreementConverterInterface;
import no.arkivverket.helsearkiv.nhareg.agreement.AgreementDAO;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.*;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    @Inject
    private AgreementDAO agreementDAO;
    
    @Inject
    private TransferConverterInterface transferConverter;

    @Inject
    private AgreementConverterInterface agreementConverter;

    @Test(expected = EntityExistsException.class)
    public void create_duplicateEntry_shouldThrowEntityExistsException() {
        final TransferDTO transferDTO = new TransferDTO();
        final Set<MedicalRecord> medicalRecords = new HashSet<>();
        final MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setUuid("test");
        medicalRecord.setPid("123");
        medicalRecord.setName("patient");
        medicalRecord.setGender(new Gender());
        medicalRecord.setStorageUnit(Collections.singleton(new StorageUnit()));
        medicalRecords.add(medicalRecord);
        
        final Agreement agreement = agreementDAO.fetchById("Avtale1");
        transferDTO.setTransferId("Avlevering-1");
        transferDTO.setTransferDescription("test");
        transferDTO.setAgreement(agreement);
        transferDTO.setMedicalRecords(medicalRecords);

        final TransferDTO newTransfer = transferService.create(transferDTO, "nhabruker1");
        
        assertNotNull(newTransfer);
        assertNotNull(newTransfer.getMedicalRecords());
        assertNotNull(newTransfer.getAgreement());
    }

    @Test
    public void getById_getValidId_shouldReturnTransfer() {
        final TransferDTO transferDTO = transferService.getById("Avlevering-1");

        assertNotNull(transferDTO);
        assertNotNull(transferDTO.getMedicalRecords());
        assertNotNull(transferDTO.getAgreement());
        assertNotNull(transferDTO.getUpdateInfo());
        transferDTO.getMedicalRecords().forEach(medicalRecord -> {
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
        final Transfer transfer = transferConverter.toTransfer(transferDTO, null);
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
        assertNotNull(updatedTransferDTO);
        assertNotNull(updatedTransferDTO.getAgreement());
        assertNotNull(updatedTransferDTO.getUpdateInfo());
    }

    @Test
    public void getTransferForStorageUnit_validId_shouldReturnTransfer() {
        final String storageId = "boks3";

        final TransferDTO transferDTO = transferService.getTransferForStorageUnit(storageId);
        
        assertNotNull(transferDTO);
        assertNotNull(transferDTO.getMedicalRecords());
        assertEquals("Avlevering-2", transferDTO.getTransferId());
    }

}