package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.CaseReferenceAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.ReferencePeriodAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
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

    @XmlElement(name = "avleveringsidentifikator")
    private Identifier transferId;
    
    @XmlElement(name = "journalidentifikator")
    private Identifier recordId;
    
    @XmlJavaTypeAdapter(value = CaseReferenceAdapter.class)
    @XmlElement(name = "saksreferanser")
    private Set<CaseReference> caseReferences = new HashSet<>();
    
    @XmlJavaTypeAdapter(value = ReferencePeriodAdapter.class)
    @XmlElement(name = "henvisningsperiode")
    private Set<ReferencePeriod> referencePeriods = new HashSet<>();

}