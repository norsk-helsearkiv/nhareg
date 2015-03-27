
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


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
public class Supplerendeopplysninger implements Serializable
{

    @XmlElement(required = true)
    protected Hendelse hendelse;
    @XmlElement(required = true)
    protected Henvisning henvisning;

    /**
     * Gets the value of the hendelse property.
     * 
     * @return
     *     possible object is
     *     {@link Hendelse }
     *     
     */
    public Hendelse getHendelse() {
        return hendelse;
    }

    /**
     * Sets the value of the hendelse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Hendelse }
     *     
     */
    public void setHendelse(Hendelse value) {
        this.hendelse = value;
    }

    /**
     * Gets the value of the henvisning property.
     * 
     * @return
     *     possible object is
     *     {@link Henvisning }
     *     
     */
    public Henvisning getHenvisning() {
        return henvisning;
    }

    /**
     * Sets the value of the henvisning property.
     * 
     * @param value
     *     allowed object is
     *     {@link Henvisning }
     *     
     */
    public void setHenvisning(Henvisning value) {
        this.henvisning = value;
    }

}
