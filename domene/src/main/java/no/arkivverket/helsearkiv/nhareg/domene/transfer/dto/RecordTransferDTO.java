package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordTransferDTO implements Serializable {

    @JsonProperty(value = "uuid")
    private String uuid;

    @JsonProperty(value = "lagringsenhet")
    private String storageUnits;

    @JsonProperty(value = "fodselsnummer")
    private String pid;

    @JsonProperty(value = "fanearkid")
    private long fanearkid;

    @JsonProperty(value = "jnr")
    private String recordNumber;

    @JsonProperty(value = "lnr")
    private String serialNumber;

    @JsonProperty(value = "navn")
    private String name;

    @JsonProperty(value = "faar")
    private String bornYear;

    @JsonProperty(value = "daar")
    private String deathYear;

    @JsonProperty(value = "oppdatertAv")
    private String updatedBy;

    @JsonProperty(value = "avleveringsidentifikator")
    private String transferId;

    @JsonProperty(value = "opprettetDato")
    private Long creationDate;

    @JsonProperty(value = "avleveringLaast")
    private boolean transferLocked;

}
