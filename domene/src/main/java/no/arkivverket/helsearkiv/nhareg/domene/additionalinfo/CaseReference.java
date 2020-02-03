package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@XmlType(
    namespace = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup",
    name = "saksreferanser",
    propOrder = {
        "xmlns",
        "caseReferenceDate",
        "caseReferenceNumber",
        "caseReferenceText",
    })
@XmlAccessorType(value = XmlAccessType.FIELD)
public class CaseReference implements Serializable {

    @XmlAttribute(name = "xmlns")
    private String xmlns = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup";
    
    @NotNull
    @XmlElement(name = "saksereferansedato")
    private LocalDate caseReferenceDate;

    @XmlElement(name = "saksreferansenummer")
    private String caseReferenceNumber;

    @NotNull
    @XmlElement(name = "saksreferansetekst")
    private String caseReferenceText;

}