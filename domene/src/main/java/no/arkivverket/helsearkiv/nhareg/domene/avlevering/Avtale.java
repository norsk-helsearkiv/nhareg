
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for Avtale complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Avtale">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="avtaleidentifikator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="avtaledato" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="avtalebeskrivelse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="virksomhet" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Virksomhet"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Avtale", propOrder = {
    "avtaleidentifikator",
    "avtaledato",
    "avtalebeskrivelse",
    "virksomhet"
})
public class Avtale implements Serializable
{

    @XmlElement(required = true)
    protected String avtaleidentifikator;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar avtaledato;
    @XmlElement(required = true)
    protected String avtalebeskrivelse;
    @XmlElement(required = true)
    protected Virksomhet virksomhet;

    /**
     * Gets the value of the avtaleidentifikator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvtaleidentifikator() {
        return avtaleidentifikator;
    }

    /**
     * Sets the value of the avtaleidentifikator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvtaleidentifikator(String value) {
        this.avtaleidentifikator = value;
    }

    /**
     * Gets the value of the avtaledato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getAvtaledato() {
        return avtaledato;
    }

    /**
     * Sets the value of the avtaledato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvtaledato(Calendar value) {
        this.avtaledato = value;
    }

    /**
     * Gets the value of the avtalebeskrivelse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvtalebeskrivelse() {
        return avtalebeskrivelse;
    }

    /**
     * Sets the value of the avtalebeskrivelse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvtalebeskrivelse(String value) {
        this.avtalebeskrivelse = value;
    }

    /**
     * Gets the value of the virksomhet property.
     * 
     * @return
     *     possible object is
     *     {@link Virksomhet }
     *     
     */
    public Virksomhet getVirksomhet() {
        return virksomhet;
    }

    /**
     * Sets the value of the virksomhet property.
     * 
     * @param value
     *     allowed object is
     *     {@link Virksomhet }
     *     
     */
    public void setVirksomhet(Virksomhet value) {
        this.virksomhet = value;
    }

}
