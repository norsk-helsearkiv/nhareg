package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 
 *  Benyttes for registrering av koder i kodeverk som identifiseres entydig av en OID, og som det ikke er naturlig 
 *  å benytte datatype CS for.  
 *  Merk:  
 *  Bruk av datatype CV forutsettes at i spesifikasjonene er angitt hvilke konkrete kodeverk som skal kunne benyttes
 *  ved registrering i dette attributtet. Denne datatypen skal kun benyttes for kodeverk som kan identifiseres 
 *  gjennom en OID.  
 *
 */
@XmlTransient
@XmlType(name = "CV", propOrder = {
    "code",
    "codeSystem",
})
@XmlSeeAlso({
    DiagnosisCode.class
})
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class CV implements Serializable {
    
    @NotNull
    @Id
    protected String code;
    
    @NotNull
    @Id
    protected String codeSystem;
    
    @NotNull
    @Id
    protected String codeSystemVersion;
    
    protected String displayName;
    
    protected String originalText;

}