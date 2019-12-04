
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;


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
    "fanearkid",
    "merknad"
})
@Data
public class Pasientjournal implements Serializable {

    @XmlElement(required = true)
    protected Journalidentifikator journalidentifikator;

    @XmlElement(required = true)
    protected Grunnopplysninger grunnopplysninger;

    @XmlElement(required = true)
    protected List<Lagringsenhet> lagringsenhet;

    @XmlElement(required = true)
    protected Oppdateringsinfo oppdateringsinfo;

    @XmlAttribute(name = "uuid")
    protected String uuid;

    @XmlElement(name="merknad")
    protected String merknad;

    @XmlTransient
    protected Calendar opprettetDato;

    protected List<Supplerendeopplysninger> supplerendeopplysninger;

    protected Set<Diagnose> diagnose;
    
    protected Boolean slettet;
    
    protected String fanearkid;

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

    @XmlTransient
    public Calendar getOpprettetDato() {
        return opprettetDato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pasientjournal that = (Pasientjournal) o;

        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

}
