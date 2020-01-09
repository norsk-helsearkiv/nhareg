package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Oppdateringsinfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Transfer;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
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
    private Oppdateringsinfo updateInfo;

    public TransferDTO(final Transfer transfer) {
        this.transferId = transfer.getTransferId();
        this.transferDescription = transfer.getTransferDescription();
        this.agreement = transfer.getAgreement();
        this.archiveCreator = transfer.getArkivskaper();
        this.medicalRecords = transfer.getMedicalRecords();
        this.medicalRecordCount = transfer.getMedicalRecords().size();
        this.locked = transfer.isLocked();
        this.storageUnitFormat = transfer.getStorageUnitFormat();
        this.updateInfo = transfer.getOppdateringsinfo();
    }

    public Transfer toTransfer() {
        final Transfer transfer = new Transfer();
        transfer.setStorageUnitFormat(getStorageUnitFormat());
        transfer.setArkivskaper(getArchiveCreator());
        transfer.setMedicalRecords(getMedicalRecords());
        transfer.setTransferDescription(getTransferDescription());
        transfer.setTransferId(getTransferId());
        transfer.setAgreement(getAgreement());
        transfer.setLocked(isLocked());
        transfer.setOppdateringsinfo(getUpdateInfo());

        return transfer;
    }

}