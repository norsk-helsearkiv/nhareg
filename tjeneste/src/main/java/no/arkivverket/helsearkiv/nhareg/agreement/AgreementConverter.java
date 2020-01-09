package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;

import java.util.List;
import java.util.stream.Collectors;

public class AgreementConverter {

    static Agreement convertToAgreement(final AgreementDTO agreementDTO) {
        return new Agreement(agreementDTO.getAgreementId(), agreementDTO.getAgreementDate(),
                             agreementDTO.getAgreementDescription(), agreementDTO.getBusiness());
    }

    public static List<Agreement> convertToAgreementList(final List<AgreementDTO> agreementDTOS) {
        return agreementDTOS.stream().map(AgreementConverter::convertToAgreement).collect(Collectors.toList());
    }

    static AgreementDTO convertToAgreementDTO(final Agreement agreement) {
        return new AgreementDTO(
            agreement.getAgreementId(),
            agreement.getAgreementDate(),
            agreement.getAgreementDescription(),
            agreement.getBusiness()
        );
    }

    static List<AgreementDTO> convertToAgreementDTOList(final List<Agreement> agreements) {
        return agreements.stream().map(AgreementConverter::convertToAgreementDTO).collect(Collectors.toList());
    }
    
}