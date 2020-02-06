package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.LocalDateTimeAdapter;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlType(propOrder = {
    "episodeId",
    "seriesId",
    "admittedDateTime",
    "dischargeDateTime",
    "episodeSubject",
    "careLevel",
    "municipalNrHome",
    "district",
    "address",
})
@XmlAccessorType(value = XmlAccessType.FIELD)
public abstract class Episode implements Serializable {

    @NotNull
    @XmlAttribute(name = "episodeID")
    private String episodeId;

    @XmlAttribute(name = "serieID")
    private String seriesId;

    @NotNull
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    @XmlElement(name = "innDatoTid")
    private LocalDateTime admittedDateTime;

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    @XmlElement(name = "utDatoTid")
    private LocalDateTime dischargeDateTime;

    @XmlElement(name = "episodeFag")
    private EpisodeSubject episodeSubject;
    
    @NotNull
    @XmlElement(name = "omsorgsniva")
    private CareLevel careLevel;
    
    @NotNull
    @XmlElement(name = "komNrHjem")
    private String municipalNrHome;
    
    @XmlElement(name = "bydel")
    private String district;
    
    @XmlElement(name = "adresse")
    private String address;
    
}