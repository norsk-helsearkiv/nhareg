package no.arkivverket.helsearkiv.nhareg.domene.lmr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LmrDTO {
    
    private String firstName;
    
    private String lastName;
    
    private String middleName;
    
    private LocalDate deathDate;
    
}