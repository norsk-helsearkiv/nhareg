package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.DiagnosisCodingSystemAdapter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "diagnose", propOrder = {
    "diagnosisDate",
    "diagnosisCode",
    "diagnosisText",
    "diagnosisCodingSystem",
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diagnose")
public class Diagnosis implements Serializable {

    @Id
    @XmlTransient
    private String uuid;
    
    @XmlElement(name = "diagnosedato")
    private DateOrYear diagnosisDate;
    
    @Column(name = "diagnosekode_code")
    @XmlElement(name = "diagnosekode")
    private String diagnosisCode;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "diagnosetekst")
    @XmlElement(required = true, name = "diagnosetekst")
    private String diagnosisText;

    @Column(name = "diagnosekode_codeSystem")
    @XmlElement(name = "diagnosekodeverk")
    @XmlJavaTypeAdapter(DiagnosisCodingSystemAdapter.class)
    private String diagnosisCodingSystem;

    @Column(name = "diagnosekode_codeSystemVersion")
    @XmlTransient
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