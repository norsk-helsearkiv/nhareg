package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordDTO {
    
    @JsonProperty(value = "persondata")
    private PersondataDTO personalDataDTO;

    @JsonProperty(value = "diagnoser")
    private List<DiagnoseDTO> diagnosisDTOList;
 
    @JsonProperty(value = "virksomhet")
    private String business;

    @JsonProperty(value = "avtaleBeskrivelse")
    private String agreementDescription;

    @JsonProperty(value = "avleveringBeskrivelse")
    private String transferDescription;

    @JsonProperty(value = "avleveringsidentifikator")
    private String transferId;

    @JsonProperty(value = "lagringsenhetformat")
    private String storageUnitFormat;

    @JsonProperty(value = "avleveringLaast")
    private boolean transferLocked;

}