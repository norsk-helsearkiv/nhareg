//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.27 at 12:13:25 PM CET 
//


package no.arkivverket.helsearkiv.nhareg.domene.avleveringsdokumentasjon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 En instans av denne klassen benyttes for å registrere opplysninger om den produksjon av arkivuttrekk for et sett enkeltpasienter og overføring disse til arkivdepot som blir foretatt på grunnlag av en avtale om avlevering inngått mellom arkivskaper og arkivdepot.  
 *             
 * 
 * <p>Java class for Avleveringsdokumentasjon complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Avleveringsdokumentasjon">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon}Avleveringsoversikt" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon}Overføring_til_arkivdepot" minOccurs="0"/>
 *         &lt;element ref="{http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon}Oppdateringsinfo"/>
 *         &lt;element ref="{http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon}Avleveringsbeskrivelse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Avleveringsdokumentasjon", propOrder = {
    "avleveringsoversikt",
    "overf\u00f8ringTilArkivdepot",
    "oppdateringsinfo",
    "avleveringsbeskrivelse"
})
public class Avleveringsdokumentasjon
    implements Serializable
{

    @XmlElement(name = "Avleveringsoversikt", namespace = "http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon")
    protected List<Avleveringsoversikt> avleveringsoversikt;
    @XmlElement(name = "Overf\u00f8ring_til_arkivdepot", namespace = "http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon")
    protected OverføringTilArkivdepot overføringTilArkivdepot;
    @XmlElement(name = "Oppdateringsinfo", namespace = "http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon", required = true)
    protected Oppdateringsinfo oppdateringsinfo;
    @XmlElement(name = "Avleveringsbeskrivelse", namespace = "http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon", required = true)
    protected Avleveringsbeskrivelse avleveringsbeskrivelse;

    /**
     * Gets the value of the avleveringsoversikt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the avleveringsoversikt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAvleveringsoversikt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Avleveringsoversikt }
     * 
     * 
     */
    public List<Avleveringsoversikt> getAvleveringsoversikt() {
        if (avleveringsoversikt == null) {
            avleveringsoversikt = new ArrayList<Avleveringsoversikt>();
        }
        return this.avleveringsoversikt;
    }

    /**
     * Gets the value of the overføringTilArkivdepot property.
     * 
     * @return
     *     possible object is
     *     {@link OverføringTilArkivdepot }
     *     
     */
    public OverføringTilArkivdepot getOverføringTilArkivdepot() {
        return overføringTilArkivdepot;
    }

    /**
     * Sets the value of the overføringTilArkivdepot property.
     * 
     * @param value
     *     allowed object is
     *     {@link OverføringTilArkivdepot }
     *     
     */
    public void setOverføringTilArkivdepot(OverføringTilArkivdepot value) {
        this.overføringTilArkivdepot = value;
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
     * Gets the value of the avleveringsbeskrivelse property.
     * 
     * @return
     *     possible object is
     *     {@link Avleveringsbeskrivelse }
     *     
     */
    public Avleveringsbeskrivelse getAvleveringsbeskrivelse() {
        return avleveringsbeskrivelse;
    }

    /**
     * Sets the value of the avleveringsbeskrivelse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Avleveringsbeskrivelse }
     *     
     */
    public void setAvleveringsbeskrivelse(Avleveringsbeskrivelse value) {
        this.avleveringsbeskrivelse = value;
    }

}
