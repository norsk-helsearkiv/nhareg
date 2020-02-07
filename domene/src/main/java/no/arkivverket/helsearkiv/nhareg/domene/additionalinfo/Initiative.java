package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.SetAdapter;

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
@XmlType(name = "tiltak", propOrder = {
    "initiativeType",
    "procedures"
})
public class Initiative implements Serializable {
    
    @XmlElement(name = "typeTiltak")
    private InitiativeType initiativeType;

    @XmlJavaTypeAdapter(value = SetAdapter.ProcedureAdapter.class)
    @XmlElement(name = "prosedyre")
    private Set<Procedure> procedures = new HashSet<>();
    
}