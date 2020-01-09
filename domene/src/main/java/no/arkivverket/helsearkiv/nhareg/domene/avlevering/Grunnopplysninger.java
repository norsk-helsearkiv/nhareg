package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.adapters.DeathDateAdapter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Grunnopplysninger", propOrder = {
    "identifikator",
    "pnavn",
    "born",
    "deathDateUnknown",
    "dead",
    "kontakt",
    "gender"
})
@Data
@Embeddable
public class Grunnopplysninger implements Serializable {

    protected Identifikator identifikator;
    
    @NotNull
    @Column(name = "pnavn")
    @XmlElement(required = true, name = "pasientnavn")
    protected String pnavn;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(column = @Column(name = "fdato"), name = "dato"),
        @AttributeOverride(column = @Column(name = "faar"), name = "aar")
    })
    @XmlElement(required = true, name = "fodtdato")
    protected DateOrYear born;

    @NotNull
    @Valid
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "kjonn")
    @XmlElement(required = true, name = "kjonn")
    protected Gender gender;

    @Basic
    @Column(name = "dodsdatoUkjent")
    @XmlElement(name = "sikkermors", nillable = true)
    @XmlJavaTypeAdapter(DeathDateAdapter.class)
    protected Boolean deathDateUnknown;

    @Basic
    @Column(name = "fodtdatoUkjent")
    @XmlTransient
    protected Boolean bornDateUnknown;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(column = @Column(name = "ddato"), name = "dato"),
        @AttributeOverride(column = @Column(name = "daar"), name = "aar")
    })
    @XmlElement(name = "morsdato", nillable = true)
    protected DateOrYear dead;

    @Embedded
    protected Kontakt kontakt;

}
