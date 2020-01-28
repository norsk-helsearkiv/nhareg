package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInfoDTO implements Serializable {

    @JsonProperty(value = "sistOppdatert")
    private String lastUpdated;

    @JsonProperty(value = "oppdatertAv")
    private String updatedBy;

    @JsonProperty(value = "prosesstrinn")
    private String processSteps;

}