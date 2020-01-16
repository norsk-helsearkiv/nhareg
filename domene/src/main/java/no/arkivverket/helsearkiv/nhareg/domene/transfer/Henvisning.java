package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Henvisning mellom pasientarkiv og relevante administrative saker i sakarkiv.
 *             
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Henvisning", propOrder = {
    "hensvisningskilde",
    "referanse",
    "beskrivelse"
})
@Data
public class Henvisning implements Serializable {

    @XmlElement(required = true)
    protected String hensvisningskilde;
    
    @XmlElement(required = true)
    protected String referanse;
    
    @XmlElement(required = true)
    protected String beskrivelse;

}
