package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO implements Serializable {
    
    @JsonProperty(value = "avleveringsidentifikator")
    private String transferId;

    @JsonProperty(value = "avleveringsbeskrivelse")
    private String transferDescription;

    @JsonProperty(value = "avtale")
    private AgreementDTO agreement;

    @JsonProperty(value = "arkivskaper")
    private String archiveAuthor;

    @JsonProperty(value = "lagringsenhetformat")
    private String storageUnitFormat;

    @JsonProperty(value = "laast")
    private boolean locked;

    @JsonProperty(value = "defaultAvlevering")
    private boolean defaultTransfer;
    
    @JsonProperty(value = "oppdateringsinfo")
    private UpdateInfoDTO updateInfo;

    @JsonProperty(value = "kanSlettes")
    private boolean canBeDeleted;

}