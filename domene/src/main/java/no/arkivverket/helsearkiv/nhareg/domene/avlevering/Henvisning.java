package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Henvisning mellom pasientarkiv og relevante administrative saker i sakarkiv.
 *             
 * 
 * <p>Java class for Henvisning complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Henvisning">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hensvisningskilde" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referanse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="beskrivelse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Henvisning", propOrder = {
    "hensvisningskilde",
    "referanse",
    "beskrivelse"
})
@Data
public class Henvisning implements Serializable {

    @XmlElement(required = true)
    protected String hensvisningskilde;
    
    @XmlElement(required = true)
    protected String referanse;
    
    @XmlElement(required = true)
    protected String beskrivelse;

}
