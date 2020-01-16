package no.arkivverket.helsearkiv.nhareg.domene.lmr;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LmrDTO {
    
    @JsonProperty(value = "fnavn")
    private String firstName;
    
    @JsonProperty(value = "enavn")
    private String lastName;
    
    @JsonProperty(value = "mnavn")
    private String middleName;
    
    @JsonProperty(value = "ddato")
    private String deathDate;
    
}