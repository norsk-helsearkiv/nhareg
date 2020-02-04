package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveRecordDTO implements Serializable {

    @JsonProperty(value = "pasientjournalUuids")
    private List<String> medicalRecordIdList;
    
    @JsonProperty(value = "lagringsenhetIdentifikator")
    private String storageUnitId;

}