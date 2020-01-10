package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferInAgreementDTO;

public interface TransferConverterInterface {
    
    Transfer toTransfer(TransferDTO transferDTO);
    
    TransferDTO fromTransfer(Transfer transfer);
    
    TransferInAgreementDTO toInAgreementDTO(Transfer transfer, Business business);
    
}