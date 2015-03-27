
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
 * Type for å representere en dato eller et årstall
 * 
 * <p>Java class for DatoEllerAar complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DatoEllerAar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="dato" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="aar">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="1800"/>
 *               &lt;maxInclusive value="2099"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatoEllerAar", propOrder = {
    "dato",
    "aar"
})
public class DatoEllerAar implements Serializable
{

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar dato;
    protected Integer aar;

    /**
     * Gets the value of the dato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getDato() {
        return dato;
    }

    /**
     * Sets the value of the dato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDato(Calendar value) {
        this.dato = value;
    }

    /**
     * Gets the value of the aar property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAar() {
        return aar;
    }

    /**
     * Sets the value of the aar property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAar(Integer value) {
        this.aar = value;
    }

}
