package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.StorageUnitAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateTimeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pasientjournal", propOrder = {
    "uuid",
    "fanearkid",    
    "recordNumber",
    "serialNumber",
    "baseProperties",
    "note",
    "diagnosis",
    "storageUnit",
    "additionalInfo",
})
@Data
@Entity
@Table(name = "pasientjournal")
public class MedicalRecord implements Serializable {

    @Column(name = "journalnummer")
    @XmlElement(required = true, name = "journalnummer", nillable = true)
    protected String recordNumber;

    @Column(name = "lopenummer")
    @XmlElement(name = "lopenummer", nillable = true)
    protected String serialNumber;

    @Embedded
    @XmlElement(required = true, name = "grunnopplysninger")
    protected BaseProperties baseProperties;

    @NotNull
    @Size(min = 1)
    @ManyToMany
    @JoinTable(name = "pasientjournal_lagringsenhet",
        joinColumns = @JoinColumn(name = "Pasientjournal_uuid"),
        inverseJoinColumns = @JoinColumn(name = "lagringsenhet_uuid")
    )
    @XmlElement(required = true, name = "lagringsenhet", nillable = true)
    @XmlJavaTypeAdapter(value = StorageUnitAdapter.class)
    protected List<StorageUnit> storageUnit;

    @Embedded
    @XmlTransient
    protected UpdateInfo updateInfo;

    @Size(min = 1)
    @Id
    @XmlElement(required = true, name = "journalidentifikator")
    protected String uuid;

    @Column(name = "merknad")
    @XmlElement(name="merknad", nillable = true)
    protected String note;

    @XmlTransient
    @Column(name = "opprettetDato", updatable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    protected LocalDateTime createdDate;

    @Transient
    @XmlElement(name = "suppleringsinfor")
    protected List<AdditionalInfo> additionalInfo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "pasientjournal_diagnose",
        joinColumns = @JoinColumn(name = "Pasientjournal_uuid"),
        inverseJoinColumns = @JoinColumn(name = "diagnose_uuid")
    )
    @XmlElement(name = "diagnose", nillable = true)
    protected Set<Diagnosis> diagnosis;

    @XmlTransient
    @Column(name = "slettet")
    protected Boolean deleted;
    
    @Column(name = "fanearkid")
    @XmlElement(name = "fanearkidentifikator", nillable = true)
    protected String fanearkid;

    @OneToOne
    @JoinColumn(name = "arkivskaper_kode", referencedColumnName = "kode")
    @XmlTransient
    protected ArchiveCreator archiveCreator;
    
    public Set<Diagnosis> getDiagnosis() {
        if (diagnosis == null) {
            diagnosis = new HashSet<>();
        }
        
        return this.diagnosis;
    }

    public List<AdditionalInfo> getAdditionalInfo() {
        if (additionalInfo == null) {
            additionalInfo = new ArrayList<>();
        }
        
        return this.additionalInfo;
    }

    public List<StorageUnit> getStorageUnit() {
        if (storageUnit == null) {
            storageUnit = new ArrayList<>();
        }
        
        return this.storageUnit;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        final MedicalRecord that = (MedicalRecord) other;

        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

}