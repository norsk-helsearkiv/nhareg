package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Supplerendeopplysninger",
    propOrder = {
    "transferId",
    "recordId",
    "caseReferences",
    "referencePeriods",
})
public class AdditionalInfo implements Serializable {

    @NotNull
    @XmlElement(name = "avleveringsidentifikator", namespace = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup")
    private String transferId;
    
    @Size(min = 1)
    @XmlElement(name = "journalidentifikator", namespace = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup")
    private String recordId;
    
    @XmlElement(name = "saksreferanser")
    private List<CaseReference> caseReferences;
    
    @XmlElement(name = "henvisningsperiode")
    private List<ReferencePeriod> referencePeriods;

}