package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.StorageUnit;

@Data
public class StorageUnitDTO {
    
    @JsonProperty(value = "identifikator")
    private String id;

    @JsonProperty(value = "uuid")
    private String uuid;

    @JsonProperty(value = "utskrift")
    private boolean print;
    
    public StorageUnitDTO(final StorageUnit storageUnit) {
        this.id = storageUnit.getId();
        this.uuid = storageUnit.getUuid();
        this.print = storageUnit.isPrint();        
    }

}