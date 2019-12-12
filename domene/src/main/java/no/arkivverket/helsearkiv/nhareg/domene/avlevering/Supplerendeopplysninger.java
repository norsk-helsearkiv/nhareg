package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * <p>Java class for Supplerendeopplysninger complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Supplerendeopplysninger">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hendelse" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Hendelse"/>
 *         &lt;element name="henvisning" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Henvisning"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Supplerendeopplysninger", propOrder = {
    "hendelse",
    "henvisning"
})
@Data
public class Supplerendeopplysninger implements Serializable {

    @XmlElement(required = true)
    protected Hendelse hendelse;
    
    @XmlElement(required = true)
    protected Henvisning henvisning;

}