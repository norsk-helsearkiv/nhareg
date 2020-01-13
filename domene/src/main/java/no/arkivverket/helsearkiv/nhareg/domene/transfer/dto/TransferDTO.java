package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.UpdateInfo;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO implements Serializable {
    
    @JsonProperty(value = "avleveringsidentifikator")
    private String transferId;

    @JsonProperty(value = "avleveringsbeskrivelse")
    private String transferDescription;

    @JsonProperty(value = "avtale")
    private Agreement agreement;

    @JsonProperty(value = "arkivskaper")
    private String archiveCreator;

    @JsonProperty(value = "lagringsenhetformat")
    private String storageUnitFormat;

    @JsonProperty(value = "pasientjournal")
    private Set<MedicalRecord> medicalRecords;
    
    @JsonProperty(value = "antallPasientjournaler")
    private int medicalRecordCount;

    @JsonProperty(value = "laast")
    private boolean locked;

    @JsonProperty(value = "defaultAvlevering")
    private boolean defaultTransfer;
    
    @JsonProperty(value = "oppdateringsinfo")
    private UpdateInfo updateInfo;
    
}