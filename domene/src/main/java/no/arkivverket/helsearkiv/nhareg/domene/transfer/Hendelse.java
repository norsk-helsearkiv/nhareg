package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Hendelse", propOrder = {
    "typehelsehjelp",
    "bosted",
    "innleggelse",
    "konsultasjon"
})
@Data
public class Hendelse implements Serializable {

    @XmlElement(required = true)
    protected String typehelsehjelp;
    
    @XmlElement(required = true)
    protected Bosted bosted;
    
    protected Innleggelse innleggelse;
    
    protected Konsultasjon konsultasjon;

}