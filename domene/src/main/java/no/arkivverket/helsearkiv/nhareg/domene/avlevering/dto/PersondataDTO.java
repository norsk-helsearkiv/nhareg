package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.DagEllerAar;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersondataDTO implements Serializable {
    
    @NotNull
    @Size(min = 1)
    private String[] lagringsenheter;
    
    private String fanearkid;
    
    private String journalnummer;
    
    private String lopenummer;
    
    private String fodselsnummer;
    
    @NotNull
    @Size(min = 1)
    private String navn;
    
    @NotNull
    @Size(min = 1)
    private String kjonn;
    
    @NotNull
    @Size(min = 1)
    @DagEllerAar
    private String fodt;

    @NotNull
    @Size(min = 1)
    @DagEllerAar
    private String dod;

    @JsonProperty("fKontakt")
    @DagEllerAar
    private String firstContact;

    @JsonProperty("sKontakt")
    @DagEllerAar
    private String lastContact;

    private String uuid;

    private String merknad;

}