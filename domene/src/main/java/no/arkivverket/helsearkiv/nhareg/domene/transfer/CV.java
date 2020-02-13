package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.*;
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
@Data
@Entity
@XmlType(
    name = "CV",
    propOrder = {
        "codeSystem",
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
public class CV implements Serializable {

    @XmlTransient
    @Id
    protected String code;

    @XmlElement(name = "CodeSystem", namespace = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup-mdk")
    @Id
    protected String codeSystem;

    @XmlTransient
    @Id
    protected String codeSystemVersion;

    @XmlTransient
    protected String displayName;

    @XmlTransient
    protected String originalText;

}