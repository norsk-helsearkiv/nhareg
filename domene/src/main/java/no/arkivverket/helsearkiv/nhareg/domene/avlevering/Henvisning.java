
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


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
public class Henvisning implements Serializable
{

    @XmlElement(required = true)
    protected String hensvisningskilde;
    @XmlElement(required = true)
    protected String referanse;
    @XmlElement(required = true)
    protected String beskrivelse;

    /**
     * Gets the value of the hensvisningskilde property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHensvisningskilde() {
        return hensvisningskilde;
    }

    /**
     * Sets the value of the hensvisningskilde property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHensvisningskilde(String value) {
        this.hensvisningskilde = value;
    }

    /**
     * Gets the value of the referanse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferanse() {
        return referanse;
    }

    /**
     * Sets the value of the referanse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferanse(String value) {
        this.referanse = value;
    }

    /**
     * Gets the value of the beskrivelse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeskrivelse() {
        return beskrivelse;
    }

    /**
     * Sets the value of the beskrivelse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeskrivelse(String value) {
        this.beskrivelse = value;
    }

}
