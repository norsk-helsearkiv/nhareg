package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "avlevering")
@XmlType(name = "Avlevering", propOrder = {
    "transferId",
    "transferDescription",
    "agreement",
    "arkivskaper",
    "medicalRecords",
    "oppdateringsinfo"
})
@Data
@Entity
@Table(name = "avlevering")
public class Transfer implements Serializable {

    @Id
    @Column(name = "avleveringsidentifikator")
    @XmlElement(required = true, name = "avleveringsidentifikator")
    protected String transferId;

    @Column(name = "avleveringsbeskrivelse")
    @XmlElement(required = true, name = "avleveringsbeskrivelse")
    protected String transferDescription;

    @ManyToOne
    @JoinColumn(name = "avtale_avtaleidentifikator")
    @XmlElement(required = true, name = "avtale")
    protected Agreement agreement;

    @XmlElement(required = true)
    protected String arkivskaper;

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

    @Embedded
    protected Oppdateringsinfo oppdateringsinfo;

    public Set<MedicalRecord> getMedicalRecords() {
        if (medicalRecords == null) {
            medicalRecords = new HashSet<>();
        }
        
        return medicalRecords;
    }
    
}