package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;

import java.util.Calendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementDTO {
    
    @JsonProperty(value = "avtaleidentifikator")
    private String agreementId;

    @JsonProperty(value = "avtaledato")
    private Calendar agreementDate;

    @JsonProperty(value = "avtalebeskrivelse")
    private String agreementDescription;

    @JsonProperty(value = "virksomhet")
    private Business business;

}