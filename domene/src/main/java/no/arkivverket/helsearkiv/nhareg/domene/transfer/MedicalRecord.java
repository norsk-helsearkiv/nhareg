package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.AdditionalInfo;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateTimeConverter;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.DeathDateKnownAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.DiagnosisAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.GenderAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.StorageUnitAdapter;

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

@XmlType(
    namespace = "http://www.arkivverket.no/standarder/nha/avlxml",
    name = "pasientjournal",
    propOrder = {
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
        "storageUnits",
        "additionalInfo",
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Entity
@Table(name = "pasientjournal")
public class MedicalRecord implements Serializable {

    @NotNull
    @XmlElement(name = "journalidentifikator")
    @Id
    private String uuid;

    @XmlElement(name = "journalnummer")
    @Column(name = "journalnummer")
    private String recordNumber;

    @XmlElement(name = "lopenummer")
    @Column(name = "lopenummer")
    private String serialNumber;

    @NotNull
    @XmlElement(name = "fodselsnummer")
    @Column(name = "pid")
    private String pid;

    @XmlTransient
    @Column(name = "typePID")
    private String typePID;

    @Size(min = 1)
    @XmlElement(name = "pasientnavn")
    @Column(name = "pnavn")
    private String name;

    @XmlElement(name = "fodtdato")
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(column = @Column(name = "fdato"), name = "date"),
        @AttributeOverride(column = @Column(name = "faar"), name = "year")
    })
    private DateOrYear born;

    @NotNull
    @Valid
    @XmlElement(name = "kjonn")
    @XmlJavaTypeAdapter(value = GenderAdapter.class)
    @ManyToOne
    @JoinColumn(name = "kjonn")
    private Gender gender;

    @XmlElement(name = "sikkermors")
    @XmlJavaTypeAdapter(DeathDateKnownAdapter.class)
    @Column(name = "dodsdatoUkjent")
    private Boolean deathDateUnknown;

    @XmlTransient
    @Column(name = "fodtdatoUkjent")
    private Boolean bornDateUnknown;

    @XmlElement(name = "morsdato")
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(column = @Column(name = "ddato"), name = "date"),
        @AttributeOverride(column = @Column(name = "daar"), name = "year")
    })
    private DateOrYear dead;

    @XmlElement(name = "forstekontakt")
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "date", column = @Column(name = "foersteKontaktDato")),
        @AttributeOverride(name = "year", column = @Column(name = "foersteKontaktAar"))
    })
    private DateOrYear firstContact;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "date", column = @Column(name = "sisteKontaktDato")),
        @AttributeOverride(name = "year", column = @Column(name = "sisteKontaktAar"))
    })
    @XmlElement(name = "sistekontakt")
    private DateOrYear lastContact;

    @Size(min = 1)
    @XmlElement(name = "lagringsenhet")
    @XmlJavaTypeAdapter(value = StorageUnitAdapter.class)
    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "pasientjournal_lagringsenhet",
        joinColumns = @JoinColumn(name = "Pasientjournal_uuid"),
        inverseJoinColumns = @JoinColumn(name = "lagringsenhet_uuid")
    )
    private Set<StorageUnit> storageUnits;

    @XmlTransient
    @Embedded
    private UpdateInfo updateInfo;

    @XmlElement(name="merknad")
    @Column(name = "merknad")
    private String note;

    @XmlTransient
    @Column(name = "opprettetDato", updatable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdDate;

    @XmlElement(name = "supplerendeopplysninger")
    @Transient
    private AdditionalInfo additionalInfo;

    @XmlJavaTypeAdapter(value = DiagnosisAdapter.class)
    @XmlElement(name = "diagnose")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pasientjournal_diagnose",
        joinColumns = @JoinColumn(name = "Pasientjournal_uuid"),
        inverseJoinColumns = @JoinColumn(name = "diagnose_uuid")
    )
    private Set<Diagnosis> diagnosis;

    @XmlTransient
    @Column(name = "slettet")
    private Boolean deleted;

    @XmlElement(name = "fanearkidentifikator")
    @Column(name = "fanearkid", unique = true)
    private String fanearkid;

    @XmlTransient
    @ManyToMany
    @JoinTable(name = "pasientjournal_arkivskaper",
        joinColumns = @JoinColumn(name = "pasientjournal_uuid"),
        inverseJoinColumns = @JoinColumn(name = "arkivskaper_uuid")
    )
    private Set<ArchiveAuthor> archiveAuthors;

    public Set<Diagnosis> getDiagnosis() {
        return diagnosis == null ? diagnosis = new HashSet<>() : diagnosis;
    }

    public Set<StorageUnit> getStorageUnits() {
        return storageUnits == null ? storageUnits = new HashSet<>() : storageUnits;
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