package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.common.ValidDateFormats;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.BusinessDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AgreementConverter implements AgreementConverterInterface {

    public Agreement toAgreement(final AgreementDTO agreementDTO) {
        if (agreementDTO == null) {
            return null;
        }

        final BusinessDTO businessDTO = agreementDTO.getBusiness();
        final Business business = new Business();
        if (businessDTO != null) {
            business.setBusinessName(businessDTO.getBusinessName());
            business.setOrganizationNumber(businessDTO.getOrganizationNumber());
            business.setName(businessDTO.getName());
        }

        final String agreementDateString = agreementDTO.getAgreementDate();
        final LocalDate date = ValidDateFormats.getDate(agreementDateString);
        final LocalDateTime agreementDate = date == null ? null : date.atStartOfDay();
        
        return new Agreement(agreementDTO.getAgreementId(), agreementDate, agreementDTO.getAgreementDescription(),
                             business);
    }

    public AgreementDTO fromAgreement(final Agreement agreement) {
        if (agreement == null) {
            return null;
        }

        final Business business = agreement.getBusiness();
        final BusinessDTO businessDTO = new BusinessDTO();
        if (business != null) {
            businessDTO.setOrganizationNumber(business.getOrganizationNumber());
            businessDTO.setName(business.getName());
            businessDTO.setBusinessName(business.getBusinessName());
        }

        return new AgreementDTO(
            agreement.getAgreementId(),
            agreement.getAgreementDate().format(DateTimeFormatter.ofPattern("ddMMuuuu")),
            agreement.getAgreementDescription(),
            businessDTO
        );
    }

    public List<AgreementDTO> fromAgreementList(final List<Agreement> agreementList) {
        if (agreementList == null) {
            return null;
        }

        return agreementList.stream().map(this::fromAgreement).collect(Collectors.toList());
    }

}