package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@XmlType(name = "avdelingsopphold",
    propOrder = {
        "episodeId",
        "seriesId",
        "admittedDateTime",
        "dischargeDateTime",
        "episodeSubject",
        "careLevel",
        "municipalNrHome",
        "district",
        "address",
        "services",
        "admissionType",
        "departmentAdmissionActivity",        
    })
@XmlAccessorType(value = XmlAccessType.FIELD)
public class HospitalAdmission extends Episode implements Serializable {

    @XmlJavaTypeAdapter(value = SetAdapter.ServiceAdapter.class)
    @XmlElement(name = "tjeneste")
    private Set<Service> services = new HashSet<>();

    @XmlElement(name = "oppholdstype")
    private AdmissionType admissionType;

    @XmlElement(name = "avdoppAktivitet")
    private DepartmentAdmissionActivity departmentAdmissionActivity;
    
}