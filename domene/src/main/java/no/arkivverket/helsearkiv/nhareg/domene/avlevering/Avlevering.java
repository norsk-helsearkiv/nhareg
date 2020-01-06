package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
 *         &lt;element name="agreement" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Agreement"/>
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
@XmlRootElement
@XmlType(name = "Avlevering", propOrder = {
    "avleveringsidentifikator",
    "avleveringsbeskrivelse",
    "agreement",
    "arkivskaper",
    "pasientjournal",
    "oppdateringsinfo"
})
@Data
@Entity
@Table(name = "avlevering")
public class Avlevering implements Serializable {

    @Id
    @Column(name = "avleveringsidentifikator")
    @XmlElement(required = true)
    protected String avleveringsidentifikator;

    @XmlElement(required = true)
    protected String avleveringsbeskrivelse;

    @ManyToOne
    @JoinColumn(name = "avtale_avtaleidentifikator")
    @XmlElement(required = true, name = "avtale")
    protected Agreement agreement;

    @XmlElement(required = true)
    protected String arkivskaper;

    @XmlTransient
    protected String lagringsenhetformat;

    @OneToMany(fetch = FetchType.EAGER)
    @XmlElement(required = true)
    protected Set<Pasientjournal> pasientjournal;

    @XmlTransient
    private boolean laast = false;

    @Embedded
    protected Oppdateringsinfo oppdateringsinfo;

    public Set<Pasientjournal> getPasientjournal() {
        if (pasientjournal == null) {
            pasientjournal = new HashSet<>();
        }
        
        return pasientjournal;
    }
}