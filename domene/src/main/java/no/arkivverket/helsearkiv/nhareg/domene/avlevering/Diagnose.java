package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

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
@Data
public class Diagnose implements Serializable {

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

        return uuid.equals(diagnose.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

}