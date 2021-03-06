//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.27 at 12:13:25 PM CET 
//


package no.arkivverket.helsearkiv.nhareg.domene.avleveringsdokumentasjon;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                     En instans av denne klassen benyttes for å registrere opplysninger om en overføring av et uttrekk av opplysninger fra arkivskaper til arkivdepot.
 *                 
 * 
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="overført_dato" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="antall_pasienter_overført" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="antall_sakstypebeskrivelser_overført" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="antall_dokumenttypebeskrivelser_overført" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "overf\u00f8rtDato",
    "antallPasienterOverf\u00f8rt",
    "antallSakstypebeskrivelserOverf\u00f8rt",
    "antallDokumenttypebeskrivelserOverf\u00f8rt"
})
@XmlRootElement(name = "Overf\u00f8ring_til_arkivdepot")
public class OverføringTilArkivdepot
    implements Serializable
{

    @XmlElement(name = "overf\u00f8rt_dato", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar overførtDato;
    @XmlElement(name = "antall_pasienter_overf\u00f8rt", required = true)
    protected BigInteger antallPasienterOverført;
    @XmlElement(name = "antall_sakstypebeskrivelser_overf\u00f8rt")
    protected BigInteger antallSakstypebeskrivelserOverført;
    @XmlElement(name = "antall_dokumenttypebeskrivelser_overf\u00f8rt", required = true)
    protected BigInteger antallDokumenttypebeskrivelserOverført;

    /**
     * Gets the value of the overførtDato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getOverførtDato() {
        return overførtDato;
    }

    /**
     * Sets the value of the overførtDato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOverførtDato(Calendar value) {
        this.overførtDato = value;
    }

    /**
     * Gets the value of the antallPasienterOverført property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAntallPasienterOverført() {
        return antallPasienterOverført;
    }

    /**
     * Sets the value of the antallPasienterOverført property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAntallPasienterOverført(BigInteger value) {
        this.antallPasienterOverført = value;
    }

    /**
     * Gets the value of the antallSakstypebeskrivelserOverført property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAntallSakstypebeskrivelserOverført() {
        return antallSakstypebeskrivelserOverført;
    }

    /**
     * Sets the value of the antallSakstypebeskrivelserOverført property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAntallSakstypebeskrivelserOverført(BigInteger value) {
        this.antallSakstypebeskrivelserOverført = value;
    }

    /**
     * Gets the value of the antallDokumenttypebeskrivelserOverført property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAntallDokumenttypebeskrivelserOverført() {
        return antallDokumenttypebeskrivelserOverført;
    }

    /**
     * Sets the value of the antallDokumenttypebeskrivelserOverført property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAntallDokumenttypebeskrivelserOverført(BigInteger value) {
        this.antallDokumenttypebeskrivelserOverført = value;
    }

}
