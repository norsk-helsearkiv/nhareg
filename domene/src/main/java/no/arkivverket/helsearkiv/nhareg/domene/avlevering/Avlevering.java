
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import java.util.*;
import javax.xml.bind.annotation.*;


/**
 * Klassen er tatt ut av generering, skal det regenereres basert på XML må hashcode og equals legges inn på uuid feltet
 * og relasjon til Pasientjournal være et Set
 *
 * <p>Java class for Avlevering complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Avlevering">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="avleveringsidentifikator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="avleveringsbeskrivelse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="avtale" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Avtale"/>
 *         &lt;element name="arkivskaper" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pasientjournal" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Pasientjournal" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}oppdateringsinfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Avlevering", propOrder = {
    "avleveringsidentifikator",
    "avleveringsbeskrivelse",
    "avtale",
    "arkivskaper",
    "pasientjournal",
    "oppdateringsinfo"
})
public class Avlevering implements Serializable
{

    @XmlElement(required = true)
    protected String avleveringsidentifikator;
    @XmlElement(required = true)
    protected String avleveringsbeskrivelse;
    @XmlElement(required = true)
    protected Avtale avtale;
    @XmlElement(required = true)
    protected String arkivskaper;
    @XmlElement(required = true)
    protected String lagringsenhetformat;

    @XmlElement(required = true)
    protected Set<Pasientjournal> pasientjournal;
    protected Oppdateringsinfo oppdateringsinfo;

    @XmlTransient
    private boolean laast = false;

    /**
     * Gets the value of the avleveringsidentifikator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvleveringsidentifikator() {
        return avleveringsidentifikator;
    }

    /**
     * Sets the value of the avleveringsidentifikator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvleveringsidentifikator(String value) {
        this.avleveringsidentifikator = value;
    }

    /**
     * Gets the value of the avleveringsbeskrivelse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvleveringsbeskrivelse() {
        return avleveringsbeskrivelse;
    }

    /**
     * Sets the value of the avleveringsbeskrivelse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvleveringsbeskrivelse(String value) {
        this.avleveringsbeskrivelse = value;
    }

    /**
     * Gets the value of the avtale property.
     * 
     * @return
     *     possible object is
     *     {@link Avtale }
     *     
     */
    public Avtale getAvtale() {
        return avtale;
    }

    /**
     * Sets the value of the avtale property.
     * 
     * @param value
     *     allowed object is
     *     {@link Avtale }
     *     
     */
    public void setAvtale(Avtale value) {
        this.avtale = value;
    }

    /**
     * Gets the value of the arkivskaper property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArkivskaper() {
        return arkivskaper;
    }

    /**
     * Sets the value of the arkivskaper property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArkivskaper(String value) {
        this.arkivskaper = value;
    }

    /**
     * Gets the value of the pasientjournal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pasientjournal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPasientjournal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Pasientjournal }
     * 
     * 
     */
    public Set<Pasientjournal> getPasientjournal() {
        if (pasientjournal == null) {
            pasientjournal = new HashSet<Pasientjournal>();
        }
        return this.pasientjournal;
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

    public void setLaast(boolean laast){
        this.laast = laast;
    }

    public boolean isLaast(){
        return this.laast;
    }

    public void setLagringsenhetformat(final String lagringsenhetformat){
        this.lagringsenhetformat = lagringsenhetformat;
    }
    public String getLagringsenhetformat(){
        return this.lagringsenhetformat;
    }
}
