package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.ArchiveAuthorAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "avlxml")
@XmlType(name = "avlevering", propOrder = {
    "xmlVersion",
    "transferId",
    "transferDescription",
    "archiveAuthor",
    "agreement",
    "medicalRecords",
})
@Data
@EqualsAndHashCode(exclude = {"medicalRecords", "agreement"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "avlevering")
public class Transfer implements Serializable {

    @NotNull
    @Id
    @Column(name = "avleveringsidentifikator")
    @XmlElement(required = true, name = "avleveringsidentifikator")
    private String transferId;

    @Size(min = 1)
    @Column(name = "avleveringsbeskrivelse")
    @XmlElement(required = true, name = "avleveringsbeskrivelse")
    private String transferDescription;

    @Transient
    @XmlElement(name = "avlxmlversjon")
    private String xmlVersion = "2.16.578.1.39.100.11.2.2";
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "avtale_avtaleidentifikator", referencedColumnName = "avtaleidentifikator")
    @XmlElement(required = true, name = "avtale")
    private Agreement agreement;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "arkivskaper_uuid")
    @XmlElement(required = true, name = "arkivskaper")
    @XmlJavaTypeAdapter(value = ArchiveAuthorAdapter.class)
    private ArchiveAuthor archiveAuthor;

    @NotNull
    @XmlTransient
    @Column(name = "lagringsenhetformat")
    private String storageUnitFormat;
    
    @XmlElement(required = true, name = "pasientjournal", nillable = true)
    @OneToMany
    @JoinTable(name = "avlevering_pasientjournal",
        joinColumns = @JoinColumn(name = "Avlevering_avleveringsidentifikator"),
        inverseJoinColumns = @JoinColumn(name = "pasientjournal_uuid")
    )
    private Set<MedicalRecord> medicalRecords;

    @XmlTransient
    @Column(name = "laast")
    private boolean locked = false;

    @XmlTransient
    @Embedded
    private UpdateInfo updateInfo;

    @XmlTransient
    @Column(name = "dateGenerated")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate dateGenerated; 
    
    public Set<MedicalRecord> getMedicalRecords() {
        if (medicalRecords == null) {
            medicalRecords = new HashSet<>();
        }
        
        return medicalRecords;
    }

}