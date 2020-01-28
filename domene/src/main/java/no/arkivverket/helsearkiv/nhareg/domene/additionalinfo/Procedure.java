package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@Data
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "prosedyre", propOrder = {
    "procedureNumber",
    "procedureCode"
})
public class Procedure implements Serializable {
    
    @XmlElement(name = "prosNr")
    private Integer procedureNumber;
    
    @Size(min = 1)
    @XmlElement(name = "Prosedyrekode")
    private List<ProcedureCode> procedureCode;
    
}