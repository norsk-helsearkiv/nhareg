package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * <p>Java class for Grunnopplysninger complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Grunnopplysninger">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifikator" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Identifikator" minOccurs="0"/>
 *         &lt;element name="pnavn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="født" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}DatoEllerAar"/>
 *         &lt;element name="dødsdatoUkjent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="død" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}DatoEllerAar" minOccurs="0"/>
 *         &lt;element name="kontakt" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Kontakt" minOccurs="0"/>
 *         &lt;element name="gender" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Gender"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Grunnopplysninger", propOrder = {
    "identifikator",
    "pnavn",
    "born",
    "deathDateUnknown",
    "bornDateUnknown",
    "dead",
    "kontakt",
    "gender"
})
@Data
@Embeddable
public class Grunnopplysninger implements Serializable {

    protected Identifikator identifikator;
    
    @NotNull
    @XmlElement(required = true)
    protected String pnavn;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(column = @Column(name = "fdato"), name = "dato"),
        @AttributeOverride(column = @Column(name = "faar"), name = "aar")
    })
    @XmlElement(required = true, name = "født")
    protected DatoEllerAar born;

    @NotNull
    @Valid
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kjonn")
    @XmlElement(required = true)
    protected Gender gender;

    @Basic
    @Column(name = "dodsdatoUkjent")
    protected Boolean deathDateUnknown;

    protected Boolean bornDateUnknown;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(column = @Column(name = "ddato"), name = "dato"),
        @AttributeOverride(column = @Column(name = "daar"), name = "aar")
    })
    protected DatoEllerAar dead;

    @Embedded
    protected Kontakt kontakt;

}
