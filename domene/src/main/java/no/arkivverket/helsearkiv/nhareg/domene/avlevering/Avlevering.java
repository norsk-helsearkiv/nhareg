package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;

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
@XmlRootElement
@XmlType(name = "Avlevering", propOrder = {
    "avleveringsidentifikator",
    "avleveringsbeskrivelse",
    "avtale",
    "arkivskaper",
    "pasientjournal",
    "oppdateringsinfo"
})
@Data
public class Avlevering implements Serializable {

    @XmlElement(required = true)
    protected String avleveringsidentifikator;

    @XmlElement(required = true)
    protected String avleveringsbeskrivelse;

    @XmlElement(required = true)
    protected Avtale avtale;

    @XmlElement(required = true)
    protected String arkivskaper;

    @XmlTransient
    protected String lagringsenhetformat;

    @XmlElement(required = true)
    protected Set<Pasientjournal> pasientjournal;

    @XmlTransient
    private boolean laast = false;

    protected Oppdateringsinfo oppdateringsinfo;

    public boolean getLaast() { return laast; }
    
    public void setLaast(final boolean laast) { this.laast = laast; }

    public Set<Pasientjournal> getPasientjournal() {
        if (pasientjournal == null) {
            pasientjournal = new HashSet<Pasientjournal>();
        }
        
        return pasientjournal;
    }
}