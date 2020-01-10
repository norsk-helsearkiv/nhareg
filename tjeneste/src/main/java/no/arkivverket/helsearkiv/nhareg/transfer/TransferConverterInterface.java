package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;

public interface TransferConverterInterface {
    
    Transfer toTransfer(TransferDTO transferDTO);
    
    TransferDTO fromTransfer(Transfer transfer);
    
}