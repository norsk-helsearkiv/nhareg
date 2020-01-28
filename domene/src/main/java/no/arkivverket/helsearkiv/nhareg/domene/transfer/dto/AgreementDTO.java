package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementDTO implements Serializable {
    
    @JsonProperty(value = "avtaleidentifikator")
    private String agreementId;
    
    @JsonProperty(value = "avtaledato")
    private LocalDateTime agreementDate;

    @JsonProperty(value = "avtalebeskrivelse")
    private String agreementDescription;

    @JsonProperty(value = "virksomhet")
    private Business business;

}