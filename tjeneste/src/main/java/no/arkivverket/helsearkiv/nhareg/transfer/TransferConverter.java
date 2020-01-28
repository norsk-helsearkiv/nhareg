package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.agreement.AgreementConverter;
import no.arkivverket.helsearkiv.nhareg.agreement.AgreementConverterInterface;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveAuthor;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.UpdateInfo;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.ArchiveAuthorDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.UpdateInfoDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransferConverter implements TransferConverterInterface {

    private AgreementConverterInterface agreementConverter = new AgreementConverter();

    @Override
    public Transfer toTransfer(final TransferDTO transferDTO, final ArchiveAuthorDTO archiveAuthorDTO) {
        if (transferDTO == null) {
            return null;
        }

        final Transfer transfer = new Transfer();
        final String storageUnitFormat = transferDTO.getStorageUnitFormat();
        final String transferDescription = transferDTO.getTransferDescription();
        final String transferId = transferDTO.getTransferId();
        final Agreement agreement = agreementConverter.toAgreement(transferDTO.getAgreement());
        final boolean locked = transferDTO.isLocked();

        if (archiveAuthorDTO == null) {
            final ArchiveAuthor newArchiveAuthor = this.createArchiveCreator(transferDTO.getArchiveCreator());
            transfer.setArchiveAuthor(newArchiveAuthor);
        } else {
            final ArchiveAuthor archiveAuthor = new ArchiveAuthor(archiveAuthorDTO.getUuid(), archiveAuthorDTO.getCode(),
                                                                  archiveAuthorDTO.getName(), archiveAuthorDTO.getDescription()); 
            transfer.setArchiveAuthor(archiveAuthor);
        }

        transfer.setStorageUnitFormat(storageUnitFormat);
        transfer.setTransferDescription(transferDescription);
        transfer.setTransferId(transferId);
        transfer.setAgreement(agreement);
        transfer.setLocked(locked);

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
        final AgreementDTO agreement = agreementConverter.fromAgreement(transfer.getAgreement());
        final String archiveCreator = transfer.getArchiveAuthor() != null ? transfer.getArchiveAuthor().getName() : null;
        final boolean locked = transfer.isLocked();
        final String storageUnitFormat = transfer.getStorageUnitFormat();
        final UpdateInfo updateInfo = transfer.getUpdateInfo();
        final String updateDate = updateInfo.getLastUpdated().format(DateTimeFormatter.ofPattern("ddMMuuuu"));
        final UpdateInfoDTO updateInfoDTO = new UpdateInfoDTO(updateDate, updateInfo.getUpdatedBy(), updateInfo.getProcessSteps());

        transferDTO.setTransferId(transferId);
        transferDTO.setTransferDescription(transferDescription);
        transferDTO.setAgreement(agreement);
        transferDTO.setArchiveCreator(archiveCreator);
        transferDTO.setLocked(locked);
        transferDTO.setStorageUnitFormat(storageUnitFormat);
        transferDTO.setUpdateInfo(updateInfoDTO);
        transferDTO.setCanBeDeleted(true);

        return transferDTO;
    }

    @Override
    public List<TransferDTO> fromTransferList(final List<Transfer> transferList) {
        return transferList.stream().map(this::fromTransfer).collect(Collectors.toList());
    }

    private ArchiveAuthor createArchiveCreator(final String archiveCreatorString) {
        if (archiveCreatorString != null && !archiveCreatorString.isEmpty()) {
            return new ArchiveAuthor(UUID.randomUUID().toString(), null, archiveCreatorString, null);
        }

        return null;
    }

}