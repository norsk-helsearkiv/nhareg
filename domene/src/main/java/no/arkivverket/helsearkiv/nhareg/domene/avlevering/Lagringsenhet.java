
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;


/**
 * 
 *                 Representerer den fysiske enheten (kasse, boks etc.), hvor pasientjournalene oppbevares.
 *             
 * 
 * <p>Java class for Lagringsenhet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Lagringsenhet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifikator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="uuid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Lagringsenhet", propOrder = {
    "identifikator"
})
@Data
public class Lagringsenhet implements Serializable {

    @XmlElement(required = true)
    protected String identifikator;

    @XmlAttribute(name = "uuid")
    protected String uuid;

    @XmlTransient
    private boolean utskrift;

}
