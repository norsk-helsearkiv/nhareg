package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.AdditionalInfoAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.DeathDateKnownAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.GenderAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.StorageUnitAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.AdditionalInfo;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateTimeConverter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pasientjournal", propOrder = {
    "uuid",
    "fanearkid",    
    "serialNumber",
    "recordNumber",
    "pid",
    "name",
    "born",
    "dead",
    "deathDateUnknown",
    "gender",
    "firstContact",
    "lastContact",
    "note",
    "diagnosis",
    "storageUnit",
    "additionalInfo",
})
@Data
@Entity
@Table(name = "pasientjournal")
public class MedicalRecord implements Serializable {

    @NotNull
    @Id
    @XmlElement(required = true, name = "journalidentifikator")
    private String uuid;
    
    @Column(name = "journalnummer")
    @XmlElement(required = true, name = "journalnummer")
    private String recordNumber;

    @Column(name = "lopenummer")
    @XmlElement(name = "lopenummer")
    private String serialNumber;

    @NotNull
    @Column(name = "pid")
    @XmlElement(name = "fodselsnummer")
    private String pid;

    @Column(name = "typePID")
    @XmlTransient
    private String typePID;

    @Size(min = 1)
    @Column(name = "pnavn")
    @XmlElement(required = true, name = "pasientnavn")
    private String name;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(column = @Column(name = "fdato"), name = "date"),
        @AttributeOverride(column = @Column(name = "faar"), name = "year")
    })
    @XmlElement(required = true, name = "fodtdato")
    private DateOrYear born;

    @NotNull
    @Valid
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "kjonn")
    @XmlElement(required = true, name = "kjonn")
    @XmlJavaTypeAdapter(value = GenderAdapter.class)
    private Gender gender;

    @Column(name = "dodsdatoUkjent")
    @XmlElement(name = "sikkermors")
    @XmlJavaTypeAdapter(DeathDateKnownAdapter.class)
    private Boolean deathDateUnknown;

    @Column(name = "fodtdatoUkjent")
    @XmlTransient
    private Boolean bornDateUnknown;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(column = @Column(name = "ddato"), name = "date"),
        @AttributeOverride(column = @Column(name = "daar"), name = "year")
    })
    @XmlElement(name = "morsdato")
    private DateOrYear dead;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "date", column = @Column(name = "foersteKontaktDato")),
        @AttributeOverride(name = "year", column = @Column(name = "foersteKontaktAar"))
    })
    @XmlElement(name = "forstekontakt")
    private DateOrYear firstContact;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "date", column = @Column(name = "sisteKontaktDato")),
        @AttributeOverride(name = "year", column = @Column(name = "sisteKontaktAar"))
    })
    @XmlElement(name = "sistekontakt")
    private DateOrYear lastContact;


    @Size(min = 1)
    @XmlElement(required = true, name = "lagringsenhet")
    @XmlJavaTypeAdapter(value = StorageUnitAdapter.class)
    @ManyToMany
    @JoinTable(name = "pasientjournal_lagringsenhet",
        joinColumns = @JoinColumn(name = "Pasientjournal_uuid"),
        inverseJoinColumns = @JoinColumn(name = "lagringsenhet_uuid")
    )
    private Set<StorageUnit> storageUnit;

    @Embedded
    @XmlTransient
    private UpdateInfo updateInfo;

    @Column(name = "merknad")
    @XmlElement(name="merknad")
    private String note;

    @XmlTransient
    @Column(name = "opprettetDato", updatable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdDate;

    @Transient
    @XmlElement(name = "supplerendeopplysninger")
    @XmlJavaTypeAdapter(value = AdditionalInfoAdapter.class)
    private AdditionalInfo additionalInfo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pasientjournal_diagnose",
        joinColumns = @JoinColumn(name = "Pasientjournal_uuid"),
        inverseJoinColumns = @JoinColumn(name = "diagnose_uuid")
    )
    @XmlElement(name = "diagnose")
    private Set<Diagnosis> diagnosis;

    @XmlTransient
    @Column(name = "slettet")
    private Boolean deleted;
    
    @Column(name = "fanearkid")
    @XmlElement(name = "fanearkidentifikator")
    private String fanearkid;

    @OneToOne
    @JoinColumn(name = "arkivskaper_uuid", referencedColumnName = "uuid")
    @XmlTransient
    private ArchiveAuthor archiveAuthor;
    
    public Set<Diagnosis> getDiagnosis() {
        return diagnosis == null ? diagnosis = new HashSet<>() : diagnosis;
    }

    public Set<StorageUnit> getStorageUnit() {
        return storageUnit == null ? storageUnit = new HashSet<>() : storageUnit;
    }

    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo == null ? new AdditionalInfo() : additionalInfo;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final MedicalRecord that = (MedicalRecord) other;

        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

}