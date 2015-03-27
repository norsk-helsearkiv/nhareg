//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.27 at 12:13:25 PM CET 
//


package no.arkivverket.helsearkiv.nhareg.domene.avleveringsdokumentasjon;

import java.io.Serializable;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                     En instans av denne klassen benyttes for å registrere opplysninger om oppdatering av avleveringsbeskrivelsen.
 *                 
 * 
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sist_oppdatert" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="oppdatert_av" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="prosesstrinn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sistOppdatert",
    "oppdatertAv",
    "prosesstrinn"
})
@XmlRootElement(name = "Oppdateringsinfo")
public class Oppdateringsinfo
    implements Serializable
{

    @XmlElement(name = "sist_oppdatert", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar sistOppdatert;
    @XmlElement(name = "oppdatert_av", required = true)
    protected String oppdatertAv;
    @XmlElement(required = true)
    protected String prosesstrinn;

    /**
     * Gets the value of the sistOppdatert property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getSistOppdatert() {
        return sistOppdatert;
    }

    /**
     * Sets the value of the sistOppdatert property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSistOppdatert(Calendar value) {
        this.sistOppdatert = value;
    }

    /**
     * Gets the value of the oppdatertAv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOppdatertAv() {
        return oppdatertAv;
    }

    /**
     * Sets the value of the oppdatertAv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOppdatertAv(String value) {
        this.oppdatertAv = value;
    }

    /**
     * Gets the value of the prosesstrinn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProsesstrinn() {
        return prosesstrinn;
    }

    /**
     * Sets the value of the prosesstrinn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProsesstrinn(String value) {
        this.prosesstrinn = value;
    }

}
