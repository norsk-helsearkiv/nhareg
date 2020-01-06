package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Pasientidentifiserende opplysninger; fødselsnummer eller annet entydig identifikasjonsnummer, eventuelle virksomhetsinterne pasientnummer eller hjelpenummer mv.
 *
 * <p>Java class for Identifikator complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Identifikator">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="typePID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="D"/>
 *               &lt;enumeration value="H"/>
 *               &lt;enumeration value="F"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
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
