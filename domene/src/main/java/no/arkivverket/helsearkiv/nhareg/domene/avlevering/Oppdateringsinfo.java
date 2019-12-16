
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.Data;


/**
 * 
 *                 En instans av denne klassen benyttes for å registrere opplysninger om oppdatering av avleveringsbeskrivelsen.
 *             
 * 
 * <p>Java class for Oppdateringsinfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Oppdateringsinfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sist_oppdatert" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="oppdatert_av" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="prosesstrinn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Oppdateringsinfo", propOrder = {
    "sistOppdatert",
    "oppdatertAv",
    "prosesstrinn"
})
@Data
public class Oppdateringsinfo implements Serializable {
    @XmlElement(name = "sist_oppdatert", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar sistOppdatert;

    @XmlElement(name = "oppdatert_av", required = true)
    protected String oppdatertAv;

    @XmlElement(required = true)
    protected String prosesstrinn;

}
