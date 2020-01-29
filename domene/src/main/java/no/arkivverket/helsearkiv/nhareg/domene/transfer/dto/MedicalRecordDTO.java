package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordDTO implements Serializable {
    
    @JsonProperty(value = "persondata")
    private PersonalDataDTO personalDataDTO;

    @JsonProperty(value = "diagnoser")
    private Set<DiagnosisDTO> diagnoses;
 
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

    @JsonProperty(value = "lagringsenheter")
    private Set<StorageUnitDTO> storageUnits;
    
    @JsonProperty(value = "archiveAuthors")
    private Set<ArchiveAuthorDTO> archiveAuthors;
    
}