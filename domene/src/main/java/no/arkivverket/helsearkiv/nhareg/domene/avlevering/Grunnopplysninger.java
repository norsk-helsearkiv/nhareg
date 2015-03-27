
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Grunnopplysninger complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Grunnopplysninger">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifikator" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Identifikator" minOccurs="0"/>
 *         &lt;element name="pnavn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="født" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}DatoEllerAar"/>
 *         &lt;element name="dødsdatoUkjent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="død" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}DatoEllerAar" minOccurs="0"/>
 *         &lt;element name="kontakt" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Kontakt" minOccurs="0"/>
 *         &lt;element name="kjønn" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Kjønn"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Grunnopplysninger", propOrder = {
    "identifikator",
    "pnavn",
    "f\u00f8dt",
    "d\u00f8dsdatoUkjent",
    "d\u00f8d",
    "kontakt",
    "kj\u00f8nn"
})
public class Grunnopplysninger implements Serializable
{

    protected Identifikator identifikator;
    @XmlElement(required = true)
    protected String pnavn;
    @XmlElement(required = true)
    protected DatoEllerAar født;
    protected Boolean dødsdatoUkjent;
    protected DatoEllerAar død;
    protected Kontakt kontakt;
    @XmlElement(required = true)
    protected Kjønn kjønn;

    /**
     * Gets the value of the identifikator property.
     * 
     * @return
     *     possible object is
     *     {@link Identifikator }
     *     
     */
    public Identifikator getIdentifikator() {
        return identifikator;
    }

    /**
     * Sets the value of the identifikator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Identifikator }
     *     
     */
    public void setIdentifikator(Identifikator value) {
        this.identifikator = value;
    }

    /**
     * Gets the value of the pnavn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPnavn() {
        return pnavn;
    }

    /**
     * Sets the value of the pnavn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPnavn(String value) {
        this.pnavn = value;
    }

    /**
     * Gets the value of the født property.
     * 
     * @return
     *     possible object is
     *     {@link DatoEllerAar }
     *     
     */
    public DatoEllerAar getFødt() {
        return født;
    }

    /**
     * Sets the value of the født property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatoEllerAar }
     *     
     */
    public void setFødt(DatoEllerAar value) {
        this.født = value;
    }

    /**
     * Gets the value of the dødsdatoUkjent property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDødsdatoUkjent() {
        return dødsdatoUkjent;
    }

    /**
     * Sets the value of the dødsdatoUkjent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDødsdatoUkjent(Boolean value) {
        this.dødsdatoUkjent = value;
    }

    /**
     * Gets the value of the død property.
     * 
     * @return
     *     possible object is
     *     {@link DatoEllerAar }
     *     
     */
    public DatoEllerAar getDød() {
        return død;
    }

    /**
     * Sets the value of the død property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatoEllerAar }
     *     
     */
    public void setDød(DatoEllerAar value) {
        this.død = value;
    }

    /**
     * Gets the value of the kontakt property.
     * 
     * @return
     *     possible object is
     *     {@link Kontakt }
     *     
     */
    public Kontakt getKontakt() {
        return kontakt;
    }

    /**
     * Sets the value of the kontakt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Kontakt }
     *     
     */
    public void setKontakt(Kontakt value) {
        this.kontakt = value;
    }

    /**
     * Gets the value of the kjønn property.
     * 
     * @return
     *     possible object is
     *     {@link Kjønn }
     *     
     */
    public Kjønn getKjønn() {
        return kjønn;
    }

    /**
     * Sets the value of the kjønn property.
     * 
     * @param value
     *     allowed object is
     *     {@link Kjønn }
     *     
     */
    public void setKjønn(Kjønn value) {
        this.kjønn = value;
    }

}
