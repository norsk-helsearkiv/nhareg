package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;

import java.util.List;

public interface TransferConverterInterface {
    
    Transfer toTransfer(final TransferDTO transferDTO);

    TransferDTO fromTransfer(final Transfer transfer);

    List<TransferDTO> fromTransferList(final List<Transfer> transferList);
    
}