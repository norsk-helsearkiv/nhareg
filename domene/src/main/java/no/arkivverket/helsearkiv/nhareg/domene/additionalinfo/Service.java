package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.LocalDateTimeAdapter;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "Tjeneste", propOrder = {
    "institutionId",
    "startDateTime",
    "endDateTime",
    "serviceSubject",
    "unit",
    "initiatives",
})
public class Service implements Serializable {
    
    @XmlElement(name = "instID")
    private String institutionId;
 
    @NotNull
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    @XmlElement(name = "startDatoTid", required = true)
    private LocalDateTime startDateTime;

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    @XmlElement(name = "sluttDatoTid")
    private LocalDateTime endDateTime;
    
    @XmlElement(name = "tjenesteFag")
    private ServiceSubject serviceSubject;
    
    @XmlElement(name = "enhet")
    private Unit unit;
    
    @XmlElement(name = "tiltak")
    private List<Initiative> initiatives;
    
}