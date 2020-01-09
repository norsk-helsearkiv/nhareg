package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * <p>Java class for Hendelse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Hendelse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="typehelsehjelp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bosted" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Bosted"/>
 *         &lt;choice>
 *           &lt;element name="innleggelse" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Innleggelse"/>
 *           &lt;element name="konsultasjon" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Konsultasjon"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Hendelse", propOrder = {
    "typehelsehjelp",
    "bosted",
    "innleggelse",
    "konsultasjon"
})
@Data
public class Hendelse implements Serializable {

    @XmlElement(required = true)
    protected String typehelsehjelp;
    
    @XmlElement(required = true)
    
    protected Bosted bosted;
    
    protected Innleggelse innleggelse;
    
    protected Konsultasjon konsultasjon;

}
