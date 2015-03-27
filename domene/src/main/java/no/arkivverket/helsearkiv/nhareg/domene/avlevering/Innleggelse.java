
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.io.Serializable;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
public class Innleggelse implements Serializable
{

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar inndato;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar utdato;

    /**
     * Gets the value of the inndato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getInndato() {
        return inndato;
    }

    /**
     * Sets the value of the inndato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInndato(Calendar value) {
        this.inndato = value;
    }

    /**
     * Gets the value of the utdato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getUtdato() {
        return utdato;
    }

    /**
     * Sets the value of the utdato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUtdato(Calendar value) {
        this.utdato = value;
    }

}
