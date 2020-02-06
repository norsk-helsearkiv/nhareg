package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateConverter;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.ArchiveAuthorAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.MedicalRecordAdapter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"medicalRecords", "agreement"})
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "avlxml")
@XmlType(name = "avlevering", propOrder = {
    "xmlns",
    "xsi",
    "schema",
    "xmlVersion",
    "transferId",
    "transferDescription",
    "dateGenerated",
    "archiveAuthor",
    "agreement",
    "medicalRecords",
})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "avlevering")
public class Transfer implements Serializable {
    
    @XmlAttribute(name = "xmlns")
    @Transient
    private final String xmlns = "http://www.arkivverket.no/standarder/nha/avlxml";
    
    @XmlAttribute(name = "xmlns:xsi")
    @Transient
    private final String xsi = "http://www.w3.org/2001/XMLSchema-instance";

    @XmlAttribute(name = "xsi:schemaLocation")
    @Transient
    private final String schema = "http://www.arkivverket.no/standarder/nha/avlxml avlxml.xsd";

    @XmlElement(name = "avlxmlversjon")
    @Transient
    private String xmlVersion = "2.16.578.1.39.100.11.2.2";

    @NotNull
    @XmlElement(name = "avleveringsidentifikator")
    @Id
    @Column(name = "avleveringsidentifikator")
    private String transferId;

    @Size(min = 1)
    @XmlElement(name = "avleveringsbeskrivelse")
    @Column(name = "avleveringsbeskrivelse")
    private String transferDescription;

    @NotNull
    @XmlElement(name = "avtale")
    @ManyToOne
    @JoinColumn(name = "avtale_avtaleidentifikator", referencedColumnName = "avtaleidentifikator")
    private Agreement agreement;

    @XmlElement(name = "arkivskaper")
    @XmlJavaTypeAdapter(value = ArchiveAuthorAdapter.class)
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "arkivskaper_uuid", referencedColumnName = "uuid")
    private ArchiveAuthor archiveAuthor;

    @NotNull
    @XmlTransient
    @Column(name = "lagringsenhetformat")
    private String storageUnitFormat;
    
    @XmlElement(name = "pasientjournal")
    @XmlJavaTypeAdapter(value = MedicalRecordAdapter.class)
    @OneToMany(mappedBy = "transfer")
    private Set<MedicalRecord> medicalRecords;

    @XmlTransient
    @Column(name = "laast")
    private boolean locked = false;

    @XmlTransient
    @Embedded
    private UpdateInfo updateInfo;

    @XmlElement
    @Column(name = "dateGenerated")
    @Convert(converter = LocalDateConverter.class)
    @Transient
    private LocalDate dateGenerated = LocalDate.now();
    
    public Set<MedicalRecord> getMedicalRecords() {
        if (medicalRecords == null) {
            medicalRecords = new HashSet<>();
        }
        
        return medicalRecords;
    }

}