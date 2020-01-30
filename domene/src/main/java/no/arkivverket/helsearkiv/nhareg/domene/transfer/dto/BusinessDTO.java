package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDTO implements Serializable {

    @JsonProperty(value = "organisasjonsnummer")
    private String organizationNumber;

    @JsonProperty(value = "navn")
    private String name;

    @JsonProperty(value = "foretaksnavn")
    private String businessName;

}