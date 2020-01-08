package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.*;

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
@Entity
@Table(name = "pasientjournal")
public class Pasientjournal implements Serializable {

    @Embedded
    @XmlElement(required = true)
    protected Journalidentifikator journalidentifikator;

    @Embedded
    @XmlElement(required = true)
    protected Grunnopplysninger grunnopplysninger;

    @NotNull
    @Size(min = 1)
    @ManyToMany
    @JoinTable(name = "pasientjournal_lagringsenhet",
        joinColumns = @JoinColumn(name = "Pasientjournal_uuid"),
        inverseJoinColumns = @JoinColumn(name = "lagringsenhet_uuid")
    )
    @XmlElement(required = true)
    protected List<Lagringsenhet> lagringsenhet;

    @Embedded
    @XmlElement(required = true)
    protected Oppdateringsinfo oppdateringsinfo;

    @Id
    @XmlAttribute(name = "uuid")
    protected String uuid;

    @XmlElement(name="merknad")
    protected String merknad;

    @XmlTransient
    @Column(name = "opprettetDato", updatable = false, insertable = false)
    protected Calendar opprettetDato = Calendar.getInstance();

    @Transient
    protected List<Supplerendeopplysninger> supplerendeopplysninger;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pasientjournal_diagnose",
        joinColumns = @JoinColumn(name = "Pasientjournal_uuid"),
        inverseJoinColumns = @JoinColumn(name = "diagnose_uuid")
    )
    protected Set<Diagnose> diagnose;
    
    protected Boolean slettet;
    
    protected String fanearkid;

    public Set<Diagnose> getDiagnose() {
        if (diagnose == null) {
            diagnose = new HashSet<>();
        }
        
        return this.diagnose;
    }

    public List<Supplerendeopplysninger> getSupplerendeopplysninger() {
        if (supplerendeopplysninger == null) {
            supplerendeopplysninger = new ArrayList<>();
        }
        
        return this.supplerendeopplysninger;
    }

    public List<Lagringsenhet> getLagringsenhet() {
        if (lagringsenhet == null) {
            lagringsenhet = new ArrayList<>();
        }
        
        return this.lagringsenhet;
    }

    @XmlTransient
    public Calendar getOpprettetDato() {
        return opprettetDato;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        final Pasientjournal that = (Pasientjournal) other;

        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

}