package no.arkivverket.helsearkiv.nhareg.domene.lmr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LmrDTO {
    
    @XmlElement(name = "fnavn")
    private String firstName;
    
    @XmlElement(name = "enavn")
    private String lastName;
    
    @XmlElement(name = "mnavn")
    private String middleName;
    
    @XmlElement(name = "ddato")
    private String deathDate;
    
}