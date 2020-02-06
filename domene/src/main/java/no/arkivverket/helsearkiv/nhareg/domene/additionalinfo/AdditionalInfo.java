package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.CaseReferenceAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.ReferencePeriodAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlType(
    name = "Supplerendeopplysninger",
    propOrder = {
    "transferId",
    "recordId",
    "caseReferences",
    "referencePeriods",
})
@XmlAccessorType(value = XmlAccessType.FIELD)
public class AdditionalInfo implements Serializable {

    @XmlElement(name = "avleveringsidentifikator", namespace = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup")
    public String getTransferId() {
        if (medicalRecord != null) {
            return medicalRecord.getTransfer().getTransferId();
        }

        return null;
    }
    
    @XmlElement(name = "journalidentifikator", namespace = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup")
    public String getRecordId() {
        if (medicalRecord != null) {
            return medicalRecord.getUuid();
        }
        
        return null;
    }
    
    @XmlTransient
    private MedicalRecord medicalRecord;
    
    @XmlJavaTypeAdapter(value = CaseReferenceAdapter.class)
    @XmlElement(name = "saksreferanser")
    private Set<CaseReference> caseReferences = new HashSet<>();
    
    @XmlJavaTypeAdapter(value = ReferencePeriodAdapter.class)
    @XmlElement(name = "henvisningsperiode")
    private Set<ReferencePeriod> referencePeriods = new HashSet<>();

    public AdditionalInfo(final MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
    
}