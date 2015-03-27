
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * Klassen er tatt ut av generering, skal det regenereres basert på XML må hashcode og equals legges inn på uuid feltet
 *
 * <p>Java class for Diagnose complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Diagnose">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="diagdato" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}DatoEllerAar" minOccurs="0"/>
 *         &lt;element ref="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}diagnosekode" minOccurs="0"/>
 *         &lt;element name="diagnosetekst" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}oppdateringsinfo"/>
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
@XmlType(name = "Diagnose", propOrder = {
    "diagdato",
    "diagnosekode",
    "diagnosetekst",
    "oppdateringsinfo"
})
public class Diagnose implements Serializable
{

    protected DatoEllerAar diagdato;
    protected Diagnosekode diagnosekode;
    @XmlElement(required = true)
    protected String diagnosetekst;
    @XmlElement(required = true)
    protected Oppdateringsinfo oppdateringsinfo;
    @XmlAttribute(name = "uuid")
    protected String uuid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Diagnose diagnose = (Diagnose) o;

        if (!uuid.equals(diagnose.uuid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    /**
     * Gets the value of the diagdato property.
     * 
     * @return
     *     possible object is
     *     {@link DatoEllerAar }
     *     
     */
    public DatoEllerAar getDiagdato() {
        return diagdato;
    }

    /**
     * Sets the value of the diagdato property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatoEllerAar }
     *     
     */
    public void setDiagdato(DatoEllerAar value) {
        this.diagdato = value;
    }

    /**
     * Gets the value of the diagnosekode property.
     * 
     * @return
     *     possible object is
     *     {@link Diagnosekode }
     *     
     */
    public Diagnosekode getDiagnosekode() {
        return diagnosekode;
    }

    /**
     * Sets the value of the diagnosekode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Diagnosekode }
     *     
     */
    public void setDiagnosekode(Diagnosekode value) {
        this.diagnosekode = value;
    }

    /**
     * Gets the value of the diagnosetekst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiagnosetekst() {
        return diagnosetekst;
    }

    /**
     * Sets the value of the diagnosetekst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiagnosetekst(String value) {
        this.diagnosetekst = value;
    }

    /**
     * Gets the value of the oppdateringsinfo property.
     * 
     * @return
     *     possible object is
     *     {@link Oppdateringsinfo }
     *     
     */
    public Oppdateringsinfo getOppdateringsinfo() {
        return oppdateringsinfo;
    }

    /**
     * Sets the value of the oppdateringsinfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Oppdateringsinfo }
     *     
     */
    public void setOppdateringsinfo(Oppdateringsinfo value) {
        this.oppdateringsinfo = value;
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
