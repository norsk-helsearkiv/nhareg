package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * <p>Java class for Kontakt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Kontakt">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="foerste" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}DatoEllerAar" minOccurs="0"/>
 *         &lt;element name="siste" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}DatoEllerAar" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Kontakt", propOrder = {
    "foerste",
    "siste"
})
@Data
@Embeddable
public class Kontakt implements Serializable {

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "dato", column = @Column(name = "foersteKontaktDato")),
        @AttributeOverride(name = "aar", column = @Column(name = "foersteKontaktAar"))
    })
    protected DatoEllerAar foerste;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "dato", column = @Column(name = "sisteKontaktDato")),
        @AttributeOverride(name = "aar", column = @Column(name = "sisteKontaktAar"))
    })
    protected DatoEllerAar siste;

}
