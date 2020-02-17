package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 *
 * Kode som angir hvilken diagnose det gjelder.
 * Eksempel på kodeverk:
 * Code System: ICD-10  [2.16.840.1.113883.6.3] (icd10)
 * 
 */
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "")
@XmlRootElement(name = "diagnosekode")  
@Entity
@Table(name = "diagnosekode")
public class DiagnosisCode extends CV implements Serializable {

    public DiagnosisCode(final String code, final String codeSystem, final String codeSystemVersion,
                         final String originalText, final String displayName) {
        this.code = code;
        this.codeSystem = codeSystem;
        this.codeSystemVersion = codeSystemVersion;
        this.originalText = originalText;
        this.displayName = displayName;
    }

    @XmlElement(name = "diagnosekode")
    @Override
    public String getCode() {
        return super.getCode();
    }
    
}