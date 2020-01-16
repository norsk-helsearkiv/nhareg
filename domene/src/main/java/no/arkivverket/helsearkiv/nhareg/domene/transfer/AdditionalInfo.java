package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Supplerendeopplysninger", propOrder = {
    "hendelse",
    "henvisning"
})
@Data
public class AdditionalInfo implements Serializable {

    @XmlElement(required = true)
    protected Hendelse hendelse;
    
    @XmlElement(required = true)
    protected Henvisning henvisning;

}