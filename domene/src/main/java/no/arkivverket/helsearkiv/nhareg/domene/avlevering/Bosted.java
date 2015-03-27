
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Bosted complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bosted">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="kommune" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Kommune"/>
 *         &lt;element name="adresse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bosted", propOrder = {
    "kommune",
    "adresse"
})
public class Bosted implements Serializable
{

    @XmlElement(required = true)
    protected Kommune kommune;
    protected String adresse;

    /**
     * Gets the value of the kommune property.
     * 
     * @return
     *     possible object is
     *     {@link Kommune }
     *     
     */
    public Kommune getKommune() {
        return kommune;
    }

    /**
     * Sets the value of the kommune property.
     * 
     * @param value
     *     allowed object is
     *     {@link Kommune }
     *     
     */
    public void setKommune(Kommune value) {
        this.kommune = value;
    }

    /**
     * Gets the value of the adresse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Sets the value of the adresse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse(String value) {
        this.adresse = value;
    }

}
