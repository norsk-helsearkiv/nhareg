package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;

import java.util.List;

public interface AgreementConverterInterface {
    
    Agreement toAgreement(final AgreementDTO agreementDTO);

    List<Agreement> toAgreementList(final List<AgreementDTO> agreementDTOList);
    
    AgreementDTO fromAgreement(final Agreement agreement);
    
    List<AgreementDTO> fromAgreementList(final List<Agreement> agreementList);
    
}