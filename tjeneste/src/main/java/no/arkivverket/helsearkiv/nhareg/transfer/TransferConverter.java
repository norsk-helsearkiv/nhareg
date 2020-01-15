package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.*;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferInAgreementDTO;

import java.util.Set;
import java.util.UUID;

public class TransferConverter implements TransferConverterInterface {
    
    @Override
    public Transfer toTransfer(final TransferDTO transferDTO, final ArchiveCreator archiveCreator) {
        if (transferDTO == null) {
            return null;
        }
        
        final Transfer transfer = new Transfer();
        final String storageUnitFormat = transferDTO.getStorageUnitFormat();
        final Set<MedicalRecord> medicalRecords = transferDTO.getMedicalRecords();
        final String transferDescription = transferDTO.getTransferDescription();
        final String transferId = transferDTO.getTransferId();
        final Agreement agreement = transferDTO.getAgreement();
        final boolean locked = transferDTO.isLocked();
        final UpdateInfo updateInfo = transferDTO.getUpdateInfo();
        
        if (archiveCreator == null) {
            final ArchiveCreator newArchiveCreator = this.createArchiveCreator(transferDTO.getArchiveCreator());
            transfer.setArchiveCreator(newArchiveCreator);
        } else {
            transfer.setArchiveCreator(archiveCreator);
        }
        
        transfer.setStorageUnitFormat(storageUnitFormat);
        transfer.setMedicalRecords(medicalRecords);
        transfer.setTransferDescription(transferDescription);
        transfer.setTransferId(transferId);
        transfer.setAgreement(agreement);
        transfer.setLocked(locked);
        transfer.setUpdateInfo(updateInfo);

        return transfer;
    }

    @Override
    public TransferDTO fromTransfer(final Transfer transfer) {
        if (transfer == null) {
            return null;
        }
        
        final TransferDTO transferDTO = new TransferDTO();
        final String transferId = transfer.getTransferId();
        final String transferDescription = transfer.getTransferDescription();
        final Agreement agreement = transfer.getAgreement();
        final String archiveCreator = transfer.getArchiveCreator() != null ? transfer.getArchiveCreator().getName() : null;
        final Set<MedicalRecord> medicalRecords = transfer.getMedicalRecords();
        final int size = transfer.getMedicalRecords().size();
        final boolean locked = transfer.isLocked();
        final String storageUnitFormat = transfer.getStorageUnitFormat();
        final UpdateInfo updateInfo = transfer.getUpdateInfo();
        
        transferDTO.setTransferId(transferId);
        transferDTO.setTransferDescription(transferDescription);
        transferDTO.setAgreement(agreement);
        transferDTO.setArchiveCreator(archiveCreator);
        transferDTO.setMedicalRecords(medicalRecords);
        transferDTO.setMedicalRecordCount(size);
        transferDTO.setLocked(locked);
        transferDTO.setStorageUnitFormat(storageUnitFormat);
        transferDTO.setUpdateInfo(updateInfo);
        
        return transferDTO;
    }

    @Override
    public TransferInAgreementDTO toInAgreementDTO(final Transfer transfer, final Business business,
                                                   final AgreementDTO agreementDTO) {
        final String transferId = transfer.getTransferId();
        final String transferDescription = transfer.getTransferDescription();
        final String archiveCreator = transfer.getArchiveCreator() != null ? transfer.getArchiveCreator().getName() : null;
        final boolean locked = transfer.isLocked();
        final String storageUnitFormat = transfer.getStorageUnitFormat();
        final String businessName = business.getName();
        final boolean canBeDeleted = transfer.getMedicalRecords().size() == 0;
        
        return new TransferInAgreementDTO(transferId, transferDescription, archiveCreator, locked, false,
                                          storageUnitFormat, businessName, agreementDTO, canBeDeleted);
    }

    private ArchiveCreator createArchiveCreator(final String archiveCreatorString) {
        if (archiveCreatorString != null && !archiveCreatorString.isEmpty()) {
            int subEnd = Math.min(archiveCreatorString.length(), 3);
            final String archiveCreatorCode = archiveCreatorString.substring(0, subEnd);
 
            return new ArchiveCreator(UUID.randomUUID().toString(), archiveCreatorCode, archiveCreatorString, null);
        }
        
        return null;
    }

}