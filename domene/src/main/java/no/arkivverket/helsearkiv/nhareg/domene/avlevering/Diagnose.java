package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "diagnose")
public class Diagnose implements Serializable {

    @Id
    @XmlAttribute(name = "uuid")
    protected String uuid;
    
    protected DatoEllerAar diagdato;
    
    @ManyToOne
    protected Diagnosekode diagnosekode;
    
    @NotNull
    @Size(min = 2, max = 255)
    @XmlElement(required = true)
    protected String diagnosetekst;
    
    @XmlElement(required = true)
    protected Oppdateringsinfo oppdateringsinfo;
    

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