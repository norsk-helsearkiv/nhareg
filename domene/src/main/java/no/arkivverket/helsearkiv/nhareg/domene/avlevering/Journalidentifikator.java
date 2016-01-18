
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Journalidentifikator complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Journalidentifikator">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="journalnummer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="løpenummer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Journalidentifikator", propOrder = {
    "journalnummer",
    "l\u00f8penummer",
        "fanearkid"
})
public class Journalidentifikator implements Serializable
{

    protected String journalnummer;
    protected String løpenummer;
    protected String fanearkid;

    public String getFanearkid() {return this.fanearkid;}

    public void setFanearkid(String fanearkid){
        this.fanearkid = fanearkid;
    }
    /**
     * Gets the value of the journalnummer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJournalnummer() {
        return journalnummer;
    }

    /**
     * Sets the value of the journalnummer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJournalnummer(String value) {
        this.journalnummer = value;
    }

    /**
     * Gets the value of the løpenummer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLøpenummer() {
        return løpenummer;
    }

    /**
     * Sets the value of the løpenummer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLøpenummer(String value) {
        this.løpenummer = value;
    }

}
