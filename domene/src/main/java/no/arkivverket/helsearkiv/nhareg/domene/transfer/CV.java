package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "CV",
    propOrder = {
        "xmlns",
        "codeSystem",
    })
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class CV implements Serializable {

    @Transient
    @XmlAttribute(name = "xmlns")
    private String xmlns = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup-mdk";
    
    @NotNull
    @Id
    protected String code;

    @NotNull
    @Id
    @XmlElement(name = "CodeSystem")
    protected String codeSystem;

    @NotNull
    @Id
    protected String codeSystemVersion;

    protected String displayName;

    protected String originalText;

}