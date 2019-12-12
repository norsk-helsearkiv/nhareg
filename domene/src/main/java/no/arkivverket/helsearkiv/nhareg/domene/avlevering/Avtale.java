package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Calendar;

/**
 * <p>Java class for Avtale complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Avtale">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="avtaleidentifikator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="avtaledato" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="avtalebeskrivelse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="virksomhet" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Virksomhet"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Avtale", propOrder = {
    "avtaleidentifikator",
    "avtaledato",
    "avtalebeskrivelse",
    "virksomhet"
})
@Data
public class Avtale implements Serializable {

    @XmlElement(required = true)
    protected String avtaleidentifikator;

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar avtaledato;

    @XmlElement(required = true)
    protected String avtalebeskrivelse;

    @XmlElement(required = true)
    protected Virksomhet virksomhet;

}