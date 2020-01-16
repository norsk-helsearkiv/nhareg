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
    "foerste",
    "siste"
})
@Data
@Embeddable
public class Contact implements Serializable {

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "dato", column = @Column(name = "foersteKontaktDato")),
        @AttributeOverride(name = "aar", column = @Column(name = "foersteKontaktAar"))
    })
    @XmlElement(name = "forstekontakt")
    protected DateOrYear foerste;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "dato", column = @Column(name = "sisteKontaktDato")),
        @AttributeOverride(name = "aar", column = @Column(name = "sisteKontaktAar"))
    })
    @XmlElement(name = "sistekontakt")
    protected DateOrYear siste;

}
