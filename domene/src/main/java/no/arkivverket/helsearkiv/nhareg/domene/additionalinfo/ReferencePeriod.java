package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.SetAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
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
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ReferencePeriod implements Serializable {

    @XmlAttribute(name = "henvisningsperiodeID")
    private String id;

    @XmlAttribute(name = "xmlns")
    private final String xmlns = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup";
    
    @XmlElement(name = "henvTilInstitusjonID")
    private String refFromInstitutionId;

    @XmlElement(name = "ansienDato")
    private LocalDate ansiDate;

    @XmlElement(name = "trygdenasjon")
    private SocialSecurityCode socialSecurityCode;
    
    @XmlElement(name = "sluttDato")
    private LocalDate endDate;

    @XmlElement(name = "henvFraInstitusjonID")
    private String refToInstitutionId;

    @XmlJavaTypeAdapter(value = SetAdapter.HospitalAdmissionAdapter.class)
    @XmlElement(name = "avdelingsopphold")
    private Set<HospitalAdmission> admissions = new HashSet<>();
    
    @XmlJavaTypeAdapter(value = SetAdapter.ContactAdapter.class)
    @XmlElement(name = "kontakt")
    private Set<Contact> contacts = new HashSet<>();
    
}