package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.adapters.StringDateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Calendar;

/**
 * <p>Java class for Konsultasjon complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Konsultasjon">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="kdato" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Konsultasjon", propOrder = {
    "kdato"
})
@Data
public class Konsultasjon implements Serializable {

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(StringDateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Calendar kdato;

}
