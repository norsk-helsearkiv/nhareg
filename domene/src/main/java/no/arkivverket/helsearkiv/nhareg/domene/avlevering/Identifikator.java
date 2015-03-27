
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Pasientidentifiserende opplysninger; fødselsnummer eller annet entydig identifikasjonsnummer, eventuelle virksomhetsinterne pasientnummer eller hjelpenummer mv.
 * 
 * <p>Java class for Identifikator complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Identifikator">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="typePID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="D"/>
 *               &lt;enumeration value="H"/>
 *               &lt;enumeration value="F"/>
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
@XmlType(name = "Identifikator", propOrder = {
    "pid",
    "typePID"
})
public class Identifikator implements Serializable
{

    @XmlElement(name = "PID", required = true)
    protected String pid;
    @XmlElement(required = true)
    protected String typePID;

    /**
     * Gets the value of the pid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPID() {
        return pid;
    }

    /**
     * Sets the value of the pid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPID(String value) {
        this.pid = value;
    }

    /**
     * Gets the value of the typePID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypePID() {
        return typePID;
    }

    /**
     * Sets the value of the typePID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypePID(String value) {
        this.typePID = value;
    }

}
