package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "avlxml")
@XmlType(name = "avlevering", propOrder = {
    "xmlVersion",
    "transferId",
    "transferDescription",
    "archiveCreator",
    "agreement",
    "medicalRecords",
})
@Data
@Entity
@Table(name = "avlevering")
public class Transfer implements Serializable {

    @Id
    @Column(name = "avleveringsidentifikator")
    @XmlElement(required = true, name = "avleveringsidentifikator")
    protected String transferId;

    @Size(min = 1)
    @Column(name = "avleveringsbeskrivelse")
    @XmlElement(required = true, name = "avleveringsbeskrivelse")
    protected String transferDescription;

    @Transient
    @XmlElement(name = "avlxmlversjon")
    protected String xmlVersion = "2.16.578.1.39.100.11.2.2";
    
    @ManyToOne
    @JoinColumn(name = "avtale_avtaleidentifikator")
    @XmlElement(required = true, name = "avtale")
    protected Agreement agreement;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "arkivskaper_kode", referencedColumnName = "kode")
    @XmlElement(required = true, name = "arkivskaper", nillable = true)
    protected ArchiveCreator archiveCreator;

    @XmlTransient
    @Column(name = "lagringsenhetformat")
    protected String storageUnitFormat;
    
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "avlevering_pasientjournal",
        joinColumns = @JoinColumn(name = "Avlevering_avleveringsidentifikator"),
        inverseJoinColumns = @JoinColumn(name = "pasientjournal_uuid")
    )
    @XmlElement(required = true, name = "pasientjournal")
    protected Set<MedicalRecord> medicalRecords;

    @XmlTransient
    @Column(name = "laast")
    private boolean locked = false;

    @XmlTransient
    @Embedded
    protected UpdateInfo updateInfo;

    public Set<MedicalRecord> getMedicalRecords() {
        if (medicalRecords == null) {
            medicalRecords = new HashSet<>();
        }
        
        return medicalRecords;
    }

}