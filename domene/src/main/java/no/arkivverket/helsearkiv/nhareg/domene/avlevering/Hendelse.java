
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


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
public class Hendelse implements Serializable
{

    @XmlElement(required = true)
    protected String typehelsehjelp;
    @XmlElement(required = true)
    protected Bosted bosted;
    protected Innleggelse innleggelse;
    protected Konsultasjon konsultasjon;

    /**
     * Gets the value of the typehelsehjelp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypehelsehjelp() {
        return typehelsehjelp;
    }

    /**
     * Sets the value of the typehelsehjelp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypehelsehjelp(String value) {
        this.typehelsehjelp = value;
    }

    /**
     * Gets the value of the bosted property.
     * 
     * @return
     *     possible object is
     *     {@link Bosted }
     *     
     */
    public Bosted getBosted() {
        return bosted;
    }

    /**
     * Sets the value of the bosted property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bosted }
     *     
     */
    public void setBosted(Bosted value) {
        this.bosted = value;
    }

    /**
     * Gets the value of the innleggelse property.
     * 
     * @return
     *     possible object is
     *     {@link Innleggelse }
     *     
     */
    public Innleggelse getInnleggelse() {
        return innleggelse;
    }

    /**
     * Sets the value of the innleggelse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Innleggelse }
     *     
     */
    public void setInnleggelse(Innleggelse value) {
        this.innleggelse = value;
    }

    /**
     * Gets the value of the konsultasjon property.
     * 
     * @return
     *     possible object is
     *     {@link Konsultasjon }
     *     
     */
    public Konsultasjon getKonsultasjon() {
        return konsultasjon;
    }

    /**
     * Sets the value of the konsultasjon property.
     * 
     * @param value
     *     allowed object is
     *     {@link Konsultasjon }
     *     
     */
    public void setKonsultasjon(Konsultasjon value) {
        this.konsultasjon = value;
    }

}
