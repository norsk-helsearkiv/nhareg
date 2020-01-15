package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "diagnose", propOrder = {
    "diagnosisDate",
    "diagnosisCode",
    "diagnosisText"
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diagnose")
public class Diagnosis implements Serializable {

    @Id
    @XmlTransient
    protected String uuid;
    
    @XmlElement(name = "diagnosedato", nillable = true)
    protected DateOrYear diagnosisDate;
    
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "diagnosekode_code", referencedColumnName = "code"),
        @JoinColumn(name = "diagnosekode_codeSystem", referencedColumnName = "codeSystem"),
        @JoinColumn(name = "diagnosekode_codeSystemVersion", referencedColumnName = "codeSystemVersion")
    })
    @XmlElement(name = "diagnosekode", nillable = true)
    protected DiagnosisCode diagnosisCode;
    
    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "diagnosetekst")
    @XmlElement(required = true, name = "diagnosetekst", nillable = true)
    protected String diagnosisText;
    
    @XmlTransient
    protected UpdateInfo updateInfo;
    
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