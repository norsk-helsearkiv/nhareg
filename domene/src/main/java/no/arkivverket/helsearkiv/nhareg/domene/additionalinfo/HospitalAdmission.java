package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "avdelingsopphold",
    propOrder = {
        "services",
        "admissionType",
        "departmentAdmissionActivity",        
    })
public class HospitalAdmission extends Episode implements Serializable {

    @XmlElement(name = "tjeneste")
    private List<Service> services; 

    @XmlElement(name = "oppholdstype")
    private AdmissionType admissionType;

    @XmlElement(name = "avdoppAktivitet")
    private DepartmentAdmissionActivity departmentAdmissionActivity;
    
}