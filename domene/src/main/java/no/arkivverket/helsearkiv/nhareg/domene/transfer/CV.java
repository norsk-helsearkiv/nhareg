package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * 
 *                 Benyttes for registrering av koder i kodeverk som identifiseres entydig av en OID, og som det ikke er naturlig å benytte datatype CS for.  
 *                 Merk:  
 *                 Bruk av datatype CV forutsettes at i spesifikasjonene er angitt hvilke konkrete kodeverk som skal kunne benyttes ved registrering i dette attributtet. Denne datatypen skal kun benyttes for kodeverk som kan identifiseres gjennom en OID.  
 *             
 * 
 * <p>Java class for CV complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CV">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codeSystem" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codeSystemVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="originalText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CV", propOrder = {
    "code",
    "displayName",
    "codeSystem",
    "codeSystemVersion",
    "originalText"
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
    
    protected String displayName;
    
    @NotNull
    @Id
    @XmlElement(required = true)
    protected String codeSystem;
    
    @NotNull
    @Id
    protected String codeSystemVersion;
    
    protected String originalText;

}