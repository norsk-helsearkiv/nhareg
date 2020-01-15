package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Kontakt", propOrder = {
    "firstContact",
    "lastContact"
})
@Data
@Embeddable
public class Contact implements Serializable {

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "date", column = @Column(name = "foersteKontaktDato")),
        @AttributeOverride(name = "year", column = @Column(name = "foersteKontaktAar"))
    })
    @XmlElement(name = "forstekontakt", nillable = true)
    protected DateOrYear firstContact;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "date", column = @Column(name = "sisteKontaktDato")),
        @AttributeOverride(name = "year", column = @Column(name = "sisteKontaktAar"))
    })
    @XmlElement(name = "sistekontakt", nillable = true)
    protected DateOrYear lastContact;

}