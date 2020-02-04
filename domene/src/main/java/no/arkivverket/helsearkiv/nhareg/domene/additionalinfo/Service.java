package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.InitiativeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@XmlType(name = "Tjeneste", propOrder = {
    "institutionId",
    "startDateTime",
    "endDateTime",
    "serviceSubject",
    "unit",
    "initiatives",
})
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Service implements Serializable {
    
    @XmlElement(name = "instID")
    private String institutionId;
 
    @XmlElement(name = "startDatoTid")
    private LocalDateTime startDateTime;

    @XmlElement(name = "sluttDatoTid")
    private LocalDateTime endDateTime;
    
    @XmlElement(name = "tjenesteFag")
    private ServiceSubject serviceSubject;
    
    @XmlElement(name = "enhet")
    private Unit unit;
    
    @XmlJavaTypeAdapter(value = InitiativeAdapter.class)
    @XmlElement(name = "tiltak")
    private Set<Initiative> initiatives = new HashSet<>();
    
}