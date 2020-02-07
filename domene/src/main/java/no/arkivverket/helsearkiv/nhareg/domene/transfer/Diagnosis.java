package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlType(name = "diagnose", propOrder = {
    "diagnosisDate",
    "diagnosisCode",
    "diagnosisText",
    "diagnosisCodingSystem",
})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "diagnose")
public class Diagnosis implements Serializable {

    @XmlTransient
    @Id
    private String uuid;
    
    @XmlElement(name = "diagnosedato")
    private DateOrYear diagnosisDate;
    
    @XmlElement(name = "diagnosekode")
    @Column(name = "diagnosekode_code")
    private String diagnosisCode;

    @NotNull
    @XmlElement(name = "diagnosetekst")
    @Column(name = "diagnosetekst")
    private String diagnosisText;

    @XmlElement(name = "diagnosekodeverk")
    @Column(name = "diagnosekode_codeSystem")
    private String diagnosisCodingSystem;

    @XmlTransient
    @Column(name = "diagnosekode_codeSystemVersion")
    private String diagnosisCodingSystemVersion;
    
    @XmlTransient
    private UpdateInfo updateInfo;
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        final Diagnosis diagnosis = (Diagnosis) other;

        return uuid.equals(diagnosis.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

}