package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pasientjournal", propOrder = {
    "recordId",
    "grunnopplysninger",
    "diagnosis",
    "supplerendeopplysninger",
    "storageUnit",
    "slettet",
    "fanearkid",
    "merknad"
})
@Data
@Entity
@Table(name = "pasientjournal")
public class MedicalRecord implements Serializable {

    @Embedded
    @XmlElement(required = true, name = "journalidentifikator")
    protected RecordId recordId;

    @Embedded
    @XmlElement(required = true)
    protected Grunnopplysninger grunnopplysninger;

    @NotNull
    @Size(min = 1)
    @ManyToMany
    @JoinTable(name = "pasientjournal_lagringsenhet",
        joinColumns = @JoinColumn(name = "Pasientjournal_uuid"),
        inverseJoinColumns = @JoinColumn(name = "lagringsenhet_uuid")
    )
    @XmlElement(required = true, name = "lagringsenhet")
    protected List<StorageUnit> storageUnit;

    @Embedded
    @XmlTransient
    protected Oppdateringsinfo oppdateringsinfo;

    @Id
    @XmlAttribute(name = "uuid")
    protected String uuid;

    @XmlElement(name="merknad")
    protected String merknad;

    @XmlTransient
    @Column(name = "opprettetDato", updatable = false)
    protected Calendar opprettetDato;

    @Transient
    protected List<Supplerendeopplysninger> supplerendeopplysninger;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pasientjournal_diagnose",
        joinColumns = @JoinColumn(name = "Pasientjournal_uuid"),
        inverseJoinColumns = @JoinColumn(name = "diagnose_uuid")
    )
    @XmlElement(name = "diagnose")
    protected Set<Diagnosis> diagnosis;
    
    protected Boolean slettet;
    
    @XmlElement(name = "fanearkidentifikator", nillable = true)
    protected String fanearkid;

    public Set<Diagnosis> getDiagnosis() {
        if (diagnosis == null) {
            diagnosis = new HashSet<>();
        }
        
        return this.diagnosis;
    }

    public List<Supplerendeopplysninger> getSupplerendeopplysninger() {
        if (supplerendeopplysninger == null) {
            supplerendeopplysninger = new ArrayList<>();
        }
        
        return this.supplerendeopplysninger;
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