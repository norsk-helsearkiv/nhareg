package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Journalidentifikator", propOrder = {
    "recordNumber",
    "serialNumber"
})
@Data
@Embeddable
public class RecordId implements Serializable {

    @Column(name = "journalnummer")
    @XmlElement(name = "journalnummer")
    protected String recordNumber;

    @Column(name = "lopenummer")
    @XmlElement(name = "lopenummer")
    protected String serialNumber;

}