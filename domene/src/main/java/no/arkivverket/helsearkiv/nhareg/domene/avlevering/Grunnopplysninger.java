
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;


/**
 * <p>Java class for Grunnopplysninger complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Grunnopplysninger">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifikator" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Identifikator" minOccurs="0"/>
 *         &lt;element name="pnavn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="født" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}DatoEllerAar"/>
 *         &lt;element name="dødsdatoUkjent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="død" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}DatoEllerAar" minOccurs="0"/>
 *         &lt;element name="kontakt" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Kontakt" minOccurs="0"/>
 *         &lt;element name="kjønn" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Kjønn"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Grunnopplysninger", propOrder = {
    "identifikator",
    "pnavn",
    "f\u00f8dt",
    "d\u00f8dsdatoUkjent",
    "fodtdatoUkjent",
    "d\u00f8d",
    "kontakt",
    "kj\u00f8nn"
})
@Data
public class Grunnopplysninger implements Serializable {

    protected Identifikator identifikator;
    
    @XmlElement(required = true)
    protected String pnavn;
    
    @XmlElement(required = true)
    protected DatoEllerAar født;

    @XmlElement(required = true)
    protected Kjønn kjønn;

    protected Boolean dødsdatoUkjent;

    protected Boolean fodtdatoUkjent;

    protected DatoEllerAar død;

    protected Kontakt kontakt;

}
