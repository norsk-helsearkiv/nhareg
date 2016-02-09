
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import java.util.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Klassen er tatt ut av generering, skal det regenereres basert på XML må hashcode og equals legges inn på uuid feltet
 * og relasjon til Diagnose være et Set
 *
 * <p>Java class for Pasientjournal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Pasientjournal">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="journalidentifikator" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Journalidentifikator"/>
 *         &lt;element name="grunnopplysninger" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Grunnopplysninger"/>
 *         &lt;element name="diagnose" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Diagnose" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="supplerendeopplysninger" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Supplerendeopplysninger" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lagringsenhet" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Lagringsenhet" maxOccurs="unbounded"/>
 *         &lt;element name="slettet" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
@XmlType(name = "Pasientjournal", propOrder = {
    "journalidentifikator",
    "grunnopplysninger",
    "diagnose",
    "supplerendeopplysninger",
    "lagringsenhet",
    "slettet",
    "oppdateringsinfo",
        "fanearkid"
})
public class Pasientjournal implements Serializable
{

    @XmlElement(required = true)
    protected Journalidentifikator journalidentifikator;
    @XmlElement(required = true)
    protected Grunnopplysninger grunnopplysninger;
    protected Set<Diagnose> diagnose;
    protected List<Supplerendeopplysninger> supplerendeopplysninger;
    @XmlElement(required = true)
    protected List<Lagringsenhet> lagringsenhet;
    protected Boolean slettet;
    @XmlElement(required = true)
    protected Oppdateringsinfo oppdateringsinfo;
    @XmlAttribute(name = "uuid")
    protected String uuid;
    protected String fanearkid;

    @XmlTransient
    protected Calendar opprettetDato;

    public String getFanearkid() {return this.fanearkid;}

    public void setFanearkid(String fanearkid){
        this.fanearkid = fanearkid;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pasientjournal that = (Pasientjournal) o;

        if (!uuid.equals(that.uuid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    /**
     * Gets the value of the journalidentifikator property.
     * 
     * @return
     *     possible object is
     *     {@link Journalidentifikator }
     *     
     */
    public Journalidentifikator getJournalidentifikator() {
        return journalidentifikator;
    }

    /**
     * Sets the value of the journalidentifikator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Journalidentifikator }
     *     
     */
    public void setJournalidentifikator(Journalidentifikator value) {
        this.journalidentifikator = value;
    }

    /**
     * Gets the value of the grunnopplysninger property.
     * 
     * @return
     *     possible object is
     *     {@link Grunnopplysninger }
     *     
     */
    public Grunnopplysninger getGrunnopplysninger() {
        return grunnopplysninger;
    }

    /**
     * Sets the value of the grunnopplysninger property.
     * 
     * @param value
     *     allowed object is
     *     {@link Grunnopplysninger }
     *     
     */
    public void setGrunnopplysninger(Grunnopplysninger value) {
        this.grunnopplysninger = value;
    }

    /**
     * Gets the value of the diagnose property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the diagnose property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDiagnose().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Diagnose }
     * 
     * 
     */
    public Set<Diagnose> getDiagnose() {
        if (diagnose == null) {
            diagnose = new HashSet<Diagnose>();
        }
        return this.diagnose;
    }

    /**
     * Gets the value of the supplerendeopplysninger property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supplerendeopplysninger property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupplerendeopplysninger().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Supplerendeopplysninger }
     * 
     * 
     */
    public List<Supplerendeopplysninger> getSupplerendeopplysninger() {
        if (supplerendeopplysninger == null) {
            supplerendeopplysninger = new ArrayList<Supplerendeopplysninger>();
        }
        return this.supplerendeopplysninger;
    }

    /**
     * Gets the value of the lagringsenhet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lagringsenhet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLagringsenhet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Lagringsenhet }
     * 
     * 
     */
    public List<Lagringsenhet> getLagringsenhet() {
        if (lagringsenhet == null) {
            lagringsenhet = new ArrayList<Lagringsenhet>();
        }
        return this.lagringsenhet;
    }

    /**
     * Gets the value of the slettet property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSlettet() {
        return slettet;
    }

    /**
     * Sets the value of the slettet property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSlettet(Boolean value) {
        this.slettet = value;
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

    @XmlTransient
    public Calendar getOpprettetDato() {
        return opprettetDato;
    }

    public void setOpprettetDato(Calendar opprettetDato) {
        this.opprettetDato = opprettetDato;
    }

}
