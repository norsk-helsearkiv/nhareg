package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferInAgreementDTO {

    @JsonProperty(value = "avleveringsidentifikator")
    private String transferId;

    @JsonProperty(value = "avleveringsbeskrivelse")
    private String transferDescription;

    @JsonProperty(value = "arkivskaper")
    private String archiveCreator;

    @JsonProperty(value = "laast")
    private boolean locked;

    @JsonProperty(value = "defaultAvlevering")
    private boolean defaultTransfer;

    @JsonProperty(value = "lagringsenhetformat")
    private String storageUnitFormat;
    
    @JsonProperty(value = "virksomhet")
    private String business;
   
    @JsonProperty(value = "avtale")
    private AgreementDTO agreementDTO;
    
}