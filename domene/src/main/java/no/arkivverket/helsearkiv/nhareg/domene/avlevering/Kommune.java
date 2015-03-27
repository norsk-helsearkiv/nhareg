
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Kommune complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Kommune">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="knavn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="knummer">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{4}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Kommune", propOrder = {
    "knavn",
    "knummer"
})
public class Kommune implements Serializable
{

    @XmlElement(required = true)
    protected String knavn;
    @XmlElement(required = true)
    protected String knummer;

    /**
     * Gets the value of the knavn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKnavn() {
        return knavn;
    }

    /**
     * Sets the value of the knavn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKnavn(String value) {
        this.knavn = value;
    }

    /**
     * Gets the value of the knummer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKnummer() {
        return knummer;
    }

    /**
     * Sets the value of the knummer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKnummer(String value) {
        this.knummer = value;
    }

}
