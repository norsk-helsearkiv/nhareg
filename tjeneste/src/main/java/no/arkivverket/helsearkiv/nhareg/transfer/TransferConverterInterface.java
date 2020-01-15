package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveCreator;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferInAgreementDTO;

public interface TransferConverterInterface {
    
    Transfer toTransfer(final TransferDTO transferDTO, final ArchiveCreator archiveCreator);
    
    TransferDTO fromTransfer(final Transfer transfer);
    
    TransferInAgreementDTO toInAgreementDTO(final Transfer transfer,
                                            final Business business, 
                                            final AgreementDTO agreementDTO);

}