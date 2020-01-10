package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;

import java.util.List;
import java.util.stream.Collectors;

public class AgreementConverter implements AgreementConverterInterface {

    public Agreement toAgreement(final AgreementDTO agreementDTO) {
        if (agreementDTO == null) {
            return null;
        }
        
        return new Agreement(agreementDTO.getAgreementId(), agreementDTO.getAgreementDate(),
                             agreementDTO.getAgreementDescription(), agreementDTO.getBusiness());
    }

    public List<Agreement> toAgreementList(final List<AgreementDTO> agreementDTOList) {
        if (agreementDTOList == null) {
            return null;
        }
        
        return agreementDTOList.stream().map(this::toAgreement).collect(Collectors.toList());
    }

    public AgreementDTO fromAgreement(final Agreement agreement) {
        if (agreement == null) {
            return null;
        }
        
        return new AgreementDTO(
            agreement.getAgreementId(),
            agreement.getAgreementDate(),
            agreement.getAgreementDescription(),
            agreement.getBusiness()
        );
    }

    public List<AgreementDTO> fromAgreementList(final List<Agreement> agreementList) {
        if (agreementList == null) {
            return null;
        }
        
        return agreementList.stream().map(this::fromAgreement).collect(Collectors.toList());
    }
    
}