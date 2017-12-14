
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import javax.xml.bind.annotation.*;


/**
 * 
 *                 Representerer den fysiske enheten (kasse, boks etc.), hvor pasientjournalene oppbevares.
 *             
 * 
 * <p>Java class for Lagringsenhet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Lagringsenhet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifikator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="uuid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Lagringsenhet", propOrder = {
    "identifikator"
})
public class Lagringsenhet implements Serializable
{

    @XmlElement(required = true)
    protected String identifikator;
    @XmlAttribute(name = "uuid")
    protected String uuid;
    @XmlTransient
    private boolean utskrift;

    public boolean isUtskrift() {
        return utskrift;
    }

    public void setUtskrift(boolean utskrift) {
        this.utskrift = utskrift;
    }

    /**
     * Gets the value of the identifikator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifikator() {
        return identifikator;
    }

    /**
     * Sets the value of the identifikator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifikator(String value) {
        this.identifikator = value;
    }

    /**
     * Gets the value of the uuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUuid(String value) {
        this.uuid = value;
    }

}
