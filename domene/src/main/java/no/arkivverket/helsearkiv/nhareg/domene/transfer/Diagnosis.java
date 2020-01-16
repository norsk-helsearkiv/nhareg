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
    "diagdato",
    "diagnosisCode",
    "diagnosetekst"
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diagnose")
public class Diagnosis implements Serializable {

    @Id
    @XmlAttribute(name = "uuid")
    protected String uuid;
    
    @XmlElement(name = "diagnosedato")
    protected DateOrYear diagdato;
    
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "diagnosekode_code", referencedColumnName = "code"),
        @JoinColumn(name = "diagnosekode_codeSystem", referencedColumnName = "codeSystem"),
        @JoinColumn(name = "diagnosekode_codeSystemVersion", referencedColumnName = "codeSystemVersion")
    })
    @XmlElement(name = "diagnosekode")
    protected DiagnosisCode diagnosisCode;
    
    @NotNull
    @Size(min = 2, max = 255)
    @XmlElement(required = true, name = "diagnosetekst")
    @Column(name = "diagnosetekst")
    protected String diagnosetekst;
    
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