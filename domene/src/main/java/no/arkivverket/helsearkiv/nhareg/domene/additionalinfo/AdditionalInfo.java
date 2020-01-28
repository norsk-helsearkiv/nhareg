package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "Supplerendeopplysninger",
    propOrder = {
    "transferId",
    "recordId",
    "caseReferences",
    "referencePeriods",
})
public class AdditionalInfo implements Serializable {

    @NotNull
    @XmlElement(name = "avleveringsidentifikator")
    private String transferId;
    
    @Size(min = 1)
    @XmlElement(name = "journalidentifikator")
    private String recordId;
    
    @XmlElement(name = "saksreferanser", nillable = true)
    private Set<CaseReference> caseReferences;
    
    @XmlElement(name = "henvisningsperiode", nillable = true)
    private Set<ReferencePeriod> referencePeriods;

}