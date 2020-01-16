package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Pasientidentifiserende opplysninger; fødselsnummer eller annet entydig identifikasjonsnummer, 
 * eventuelle virksomhetsinterne pasientnummer eller hjelpenummer mv.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Identifikator", propOrder = {
    "pid",
    "typePID"
})
@Data
@Embeddable
public class Identifikator implements Serializable {

    @NotNull
    @XmlElement(name = "PID", required = true)
    protected String pid;
    
    @XmlElement(required = true)
    protected String typePID;

}
