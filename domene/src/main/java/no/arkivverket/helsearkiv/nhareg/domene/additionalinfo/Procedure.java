package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.ProcedureCodeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "prosedyre", propOrder = {
    "procedureNumber",
    "procedureCode"
})
public class Procedure implements Serializable {
    
    @XmlElement(name = "prosNr")
    private Integer procedureNumber;
    
    @XmlJavaTypeAdapter(value = ProcedureCodeAdapter.class)
    @XmlElement(name = "Prosedyrekode")
    private Set<ProcedureCode> procedureCode = new HashSet<>();
    
}