package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageUnitDTO {
    
    @JsonProperty(value = "identifikator")
    private String id;

    @JsonProperty(value = "uuid")
    private String uuid;

    @JsonProperty(value = "utskrift")
    private boolean print;
    
}