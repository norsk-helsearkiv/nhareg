package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.DateOrYearConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDTO implements Serializable {

    @JsonProperty(value = "uuid")
    private String uuid;

    @JsonProperty(value = "fanearkid")
    private Long fanearkid;
    
    @JsonProperty(value = "journalnummer")
    private String recordNumber;
    
    @JsonProperty(value = "lopenummer")
    private String serialNumber;
    
    @JsonProperty(value = "fodselsnummer")
    private String pid;
    
    @NotNull
    @JsonProperty(value = "navn")
    private String name;
    
    @NotNull
    @JsonProperty(value = "kjonn")
    private String gender;
    
    @NotNull
    @DateOrYearConstraint
    @JsonProperty(value = "fodt")
    private String born;

    @NotNull
    @Size(min = 1)
    @DateOrYearConstraint
    @JsonProperty(value = "dod")
    private String dead;

    @DateOrYearConstraint
    @JsonProperty("fKontakt")
    private String firstContact;

    @DateOrYearConstraint
    @JsonProperty("sKontakt")
    private String lastContact;

    @JsonProperty(value = "merknad")
    private String note;

    @JsonProperty(value = "diagnoser")
    private Set<DiagnosisDTO> diagnoses;
    
    @JsonProperty(value = "archiveAuthors")
    private Set<ArchiveAuthorDTO> archiveAuthors;

    @NotNull
    @Size(min = 1)
    @JsonProperty(value = "lagringsenheter")
    private String[] storageUnits;

    @JsonProperty(value = "avleveringsidentifikator")
    private String transferId;

    @JsonProperty(value = "avleveringLaast")
    private boolean transferLocked;

    @JsonProperty(value = "slettet")
    private Boolean deleted;
    
}