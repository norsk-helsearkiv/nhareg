package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;


import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.LocalDateAdapter;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(
    name = "saksreferanser",
    namespace = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup",
    propOrder = {
        "caseReferenceDate",
        "caseReferenceNumber",
        "caseReferenceText",
    })
public class CaseReference implements Serializable {

    @NotNull
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    @XmlElement(name = "saksereferansedato")
    private LocalDate caseReferenceDate;

    @XmlElement(name = "saksreferansenummer")
    private String caseReferenceNumber;

    @NotNull
    @XmlElement(name = "saksreferansetekst")
    private String caseReferenceText;

}