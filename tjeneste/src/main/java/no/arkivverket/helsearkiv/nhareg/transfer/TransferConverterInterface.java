package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferInAgreementDTO;

public interface TransferConverterInterface {
    
    Transfer toTransfer(final TransferDTO transferDTO);
    
    TransferDTO fromTransfer(final Transfer transfer);
    
    TransferInAgreementDTO toInAgreementDTO(final Transfer transfer, final Business business,
                                            final Agreement agreement);
    
}