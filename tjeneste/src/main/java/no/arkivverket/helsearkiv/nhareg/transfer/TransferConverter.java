package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferInAgreementDTO;

public class TransferConverter implements TransferConverterInterface {
    
    @Override
    public Transfer toTransfer(final TransferDTO transferDTO) {
        if (transferDTO == null) {
            return null;
        }
        
        final Transfer transfer = new Transfer();
        transfer.setStorageUnitFormat(transferDTO.getStorageUnitFormat());
        transfer.setArkivskaper(transferDTO.getArchiveCreator());
        transfer.setMedicalRecords(transferDTO.getMedicalRecords());
        transfer.setTransferDescription(transferDTO.getTransferDescription());
        transfer.setTransferId(transferDTO.getTransferId());
        transfer.setAgreement(transferDTO.getAgreement());
        transfer.setLocked(transferDTO.isLocked());
        transfer.setUpdateInfo(transferDTO.getUpdateInfo());

        return transfer;
    }

    @Override
    public TransferDTO fromTransfer(final Transfer transfer) {
        if (transfer == null) {
            return null;
        }
        
        final TransferDTO transferDTO = new TransferDTO();
        transferDTO.setTransferId(transfer.getTransferId());
        transferDTO.setTransferDescription(transfer.getTransferDescription());
        transferDTO.setAgreement(transfer.getAgreement());
        transferDTO.setArchiveCreator(transfer.getArkivskaper());
        transferDTO.setMedicalRecords(transfer.getMedicalRecords());
        transferDTO.setMedicalRecordCount(transfer.getMedicalRecords().size());
        transferDTO.setLocked(transfer.isLocked());
        transferDTO.setStorageUnitFormat(transfer.getStorageUnitFormat());
        transferDTO.setUpdateInfo(transfer.getUpdateInfo());
        
        return transferDTO;
    }

    @Override
    public TransferInAgreementDTO toInAgreementDTO(final Transfer transfer, final Business business) {
        return new TransferInAgreementDTO(transfer.getTransferId(), transfer.getTransferDescription(),
                                          transfer.getArkivskaper(), transfer.isLocked(), false,
                                          transfer.getStorageUnitFormat(), business);
    }

}