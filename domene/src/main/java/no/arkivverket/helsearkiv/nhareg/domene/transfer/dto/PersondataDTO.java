package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.DateOrYear;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersondataDTO implements Serializable {
    
    @NotNull
    @Size(min = 1)
    @JsonProperty(value = "lagringsenheter")
    private String[] storageUnits;
    
    @JsonProperty(value = "fanearkid")
    private String fanearkid;
    
    @JsonProperty(value = "journalnummer")
    private String recordNumber;
    
    @JsonProperty(value = "lopenummer")
    private String serialNumber;
    
    @JsonProperty(value = "fodselsnummer")
    private String pid;
    
    @NotNull
    @Size(min = 1)
    @JsonProperty(value = "navn")
    private String name;
    
    @NotNull
    @Size(min = 1)
    @JsonProperty(value = "kjonn")
    private String gender;
    
    @NotNull
    @Size(min = 1)
    @DateOrYear
    @JsonProperty(value = "fodt")
    private String born;

    @NotNull
    @Size(min = 1)
    @DateOrYear
    @JsonProperty(value = "dod")
    private String dead;

    @DateOrYear
    @JsonProperty("fKontakt")
    private String firstContact;

    @DateOrYear
    @JsonProperty("sKontakt")
    private String lastContact;

    @JsonProperty(value = "uuid")
    private String uuid;

    @JsonProperty(value = "merknad")
    private String note;

}