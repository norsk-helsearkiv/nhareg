
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Kontakt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Kontakt">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="foerste" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}DatoEllerAar" minOccurs="0"/>
 *         &lt;element name="siste" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}DatoEllerAar" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Kontakt", propOrder = {
    "foerste",
    "siste"
})
public class Kontakt implements Serializable
{

    protected DatoEllerAar foerste;
    protected DatoEllerAar siste;

    /**
     * Gets the value of the foerste property.
     * 
     * @return
     *     possible object is
     *     {@link DatoEllerAar }
     *     
     */
    public DatoEllerAar getFoerste() {
        return foerste;
    }

    /**
     * Sets the value of the foerste property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatoEllerAar }
     *     
     */
    public void setFoerste(DatoEllerAar value) {
        this.foerste = value;
    }

    /**
     * Gets the value of the siste property.
     * 
     * @return
     *     possible object is
     *     {@link DatoEllerAar }
     *     
     */
    public DatoEllerAar getSiste() {
        return siste;
    }

    /**
     * Sets the value of the siste property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatoEllerAar }
     *     
     */
    public void setSiste(DatoEllerAar value) {
        this.siste = value;
    }

}
