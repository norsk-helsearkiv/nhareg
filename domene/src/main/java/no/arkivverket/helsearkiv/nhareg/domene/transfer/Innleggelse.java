package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.StringDateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Calendar;

/**
 * <p>Java class for Innleggelse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Innleggelse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="inndato" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="utdato" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Innleggelse", propOrder = {
    "inndato",
    "utdato"
})
@Data
public class Innleggelse implements Serializable {

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(StringDateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Calendar inndato;

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(StringDateAdapter .class)
    @XmlSchemaType(name = "date")
    protected Calendar utdato;

}
