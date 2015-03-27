
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Virksomhet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Virksomhet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="organisasjonsnummer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="navn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="foretaksnavn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Virksomhet", propOrder = {
    "organisasjonsnummer",
    "navn",
    "foretaksnavn"
})
public class Virksomhet implements Serializable
{

    @XmlElement(required = true)
    protected String organisasjonsnummer;
    @XmlElement(required = true)
    protected String navn;
    protected String foretaksnavn;

    /**
     * Gets the value of the organisasjonsnummer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    /**
     * Sets the value of the organisasjonsnummer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganisasjonsnummer(String value) {
        this.organisasjonsnummer = value;
    }

    /**
     * Gets the value of the navn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNavn() {
        return navn;
    }

    /**
     * Sets the value of the navn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNavn(String value) {
        this.navn = value;
    }

    /**
     * Gets the value of the foretaksnavn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForetaksnavn() {
        return foretaksnavn;
    }

    /**
     * Sets the value of the foretaksnavn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForetaksnavn(String value) {
        this.foretaksnavn = value;
    }

}
