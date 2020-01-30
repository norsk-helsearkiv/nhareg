package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@Data
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "tiltak", propOrder = {
    "initiativeType",
    "procedures"
})
public class Initiative implements Serializable {
    
    @NotNull
    @XmlElement(name = "typeTiltak")
    private InitiativeType initiativeType;
    
    @XmlElement(name = "prosedyre")
    private List<Procedure> procedures;
    
}