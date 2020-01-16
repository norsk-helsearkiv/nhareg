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
public class DiagnoseDTO implements Serializable {

    @JsonProperty(value = "uuid")
    private String uuid;

    @NotNull
    @Size(min = 1)
    @DateOrYear
    @JsonProperty(value = "diagnosedato")
    private String diagnosisDate;

    @JsonProperty(value = "diagnosekodeverk")
    private String diagnosisCodeSystem;
    
    @JsonProperty(value = "diagnosekode")
    private String diagnosisCode;

    @NotNull
    @Size(min = 1)
    @JsonProperty(value = "diagnosetekst")
    private String diagnosisText;

    @JsonProperty(value = "oppdatertAv")
    private String updatedBy;

    @JsonProperty(value = "oppdatertDato")
    private String updatedDate;

}