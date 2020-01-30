package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.LocalDateAdapter;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(
    propOrder = {
        "id",
        "xmlns",
        "refFromInstitutionId",
        "ansiDate",
        "socialSecurityCode",
        "endDate",
        "refToInstitutionId",
        "admissions",
})
public class ReferencePeriod implements Serializable {

    @NotNull
    @XmlAttribute(name = "henvisningsperiodeID")
    private String id;

    @XmlAttribute(name = "xmlns")
    private String xmlns = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup";
    
    @XmlElement(name = "henvTilInstitusjonID")
    private String refFromInstitutionId;

    @XmlElement(name = "ansienDato")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate ansiDate;

    @XmlElement(name = "trydgenasjon")
    private SocialSecurityCode socialSecurityCode;
    
    @XmlElement(name = "sluttDato")
    private LocalDate endDate;

    @XmlElement(name = "henvFraInstitusjonID")
    private String refToInstitutionId;

    @XmlElement(name = "episode")
    private Set<Episode> admissions;
    
}