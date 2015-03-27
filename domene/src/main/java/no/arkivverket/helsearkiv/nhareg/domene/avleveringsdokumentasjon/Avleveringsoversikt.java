//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.27 at 12:13:25 PM CET 
//


package no.arkivverket.helsearkiv.nhareg.domene.avleveringsdokumentasjon;

import java.io.Serializable;
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
 *                     Et sett instanser av denne klassen benyttes for å registrere opplysninger hvilke pasienter som inngår i et arkivuttrekk samt om overføringen av disse til arkivdepot.
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
 *         &lt;element name="pasient_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pasient_ID_type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fødselsdato" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="morsdato" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="uttrekksdato" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="kvalitetssikret_dato" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="overført_dato" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="kvittering_mottatt_dato" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="status_fra_arkivdepot" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tilbakemelding_fra_arkivdepot" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dato_godtatt_av_arkivdepot" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="dato_forkastet_av_arkivdepot" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="tidspunkt_slettet_fra_system" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element ref="{http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon}Overført_til_ny_avlevering" minOccurs="0"/>
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
    "pasientID",
    "pasientIDType",
    "f\u00f8dselsdato",
    "morsdato",
    "uttrekksdato",
    "kvalitetssikretDato",
    "overf\u00f8rtDato",
    "kvitteringMottattDato",
    "statusFraArkivdepot",
    "tilbakemeldingFraArkivdepot",
    "datoGodtattAvArkivdepot",
    "datoForkastetAvArkivdepot",
    "tidspunktSlettetFraSystem",
    "overf\u00f8rtTilNyAvlevering"
})
@XmlRootElement(name = "Avleveringsoversikt")
public class Avleveringsoversikt
    implements Serializable
{

    @XmlElement(name = "pasient_ID", required = true)
    protected String pasientID;
    @XmlElement(name = "pasient_ID_type", required = true)
    protected String pasientIDType;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar fødselsdato;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar morsdato;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar uttrekksdato;
    @XmlElement(name = "kvalitetssikret_dato", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar kvalitetssikretDato;
    @XmlElement(name = "overf\u00f8rt_dato", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar overførtDato;
    @XmlElement(name = "kvittering_mottatt_dato", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar kvitteringMottattDato;
    @XmlElement(name = "status_fra_arkivdepot")
    protected String statusFraArkivdepot;
    @XmlElement(name = "tilbakemelding_fra_arkivdepot")
    protected String tilbakemeldingFraArkivdepot;
    @XmlElement(name = "dato_godtatt_av_arkivdepot", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar datoGodtattAvArkivdepot;
    @XmlElement(name = "dato_forkastet_av_arkivdepot", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar datoForkastetAvArkivdepot;
    @XmlElement(name = "tidspunkt_slettet_fra_system", type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar tidspunktSlettetFraSystem;
    @XmlElement(name = "Overf\u00f8rt_til_ny_avlevering", namespace = "http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon")
    protected OverførtTilNyAvlevering overførtTilNyAvlevering;

    /**
     * Gets the value of the pasientID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasientID() {
        return pasientID;
    }

    /**
     * Sets the value of the pasientID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasientID(String value) {
        this.pasientID = value;
    }

    /**
     * Gets the value of the pasientIDType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasientIDType() {
        return pasientIDType;
    }

    /**
     * Sets the value of the pasientIDType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasientIDType(String value) {
        this.pasientIDType = value;
    }

    /**
     * Gets the value of the fødselsdato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getFødselsdato() {
        return fødselsdato;
    }

    /**
     * Sets the value of the fødselsdato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFødselsdato(Calendar value) {
        this.fødselsdato = value;
    }

    /**
     * Gets the value of the morsdato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getMorsdato() {
        return morsdato;
    }

    /**
     * Sets the value of the morsdato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMorsdato(Calendar value) {
        this.morsdato = value;
    }

    /**
     * Gets the value of the uttrekksdato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getUttrekksdato() {
        return uttrekksdato;
    }

    /**
     * Sets the value of the uttrekksdato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUttrekksdato(Calendar value) {
        this.uttrekksdato = value;
    }

    /**
     * Gets the value of the kvalitetssikretDato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getKvalitetssikretDato() {
        return kvalitetssikretDato;
    }

    /**
     * Sets the value of the kvalitetssikretDato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKvalitetssikretDato(Calendar value) {
        this.kvalitetssikretDato = value;
    }

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
     * Gets the value of the kvitteringMottattDato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getKvitteringMottattDato() {
        return kvitteringMottattDato;
    }

    /**
     * Sets the value of the kvitteringMottattDato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKvitteringMottattDato(Calendar value) {
        this.kvitteringMottattDato = value;
    }

    /**
     * Gets the value of the statusFraArkivdepot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusFraArkivdepot() {
        return statusFraArkivdepot;
    }

    /**
     * Sets the value of the statusFraArkivdepot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusFraArkivdepot(String value) {
        this.statusFraArkivdepot = value;
    }

    /**
     * Gets the value of the tilbakemeldingFraArkivdepot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTilbakemeldingFraArkivdepot() {
        return tilbakemeldingFraArkivdepot;
    }

    /**
     * Sets the value of the tilbakemeldingFraArkivdepot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTilbakemeldingFraArkivdepot(String value) {
        this.tilbakemeldingFraArkivdepot = value;
    }

    /**
     * Gets the value of the datoGodtattAvArkivdepot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getDatoGodtattAvArkivdepot() {
        return datoGodtattAvArkivdepot;
    }

    /**
     * Sets the value of the datoGodtattAvArkivdepot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatoGodtattAvArkivdepot(Calendar value) {
        this.datoGodtattAvArkivdepot = value;
    }

    /**
     * Gets the value of the datoForkastetAvArkivdepot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getDatoForkastetAvArkivdepot() {
        return datoForkastetAvArkivdepot;
    }

    /**
     * Sets the value of the datoForkastetAvArkivdepot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatoForkastetAvArkivdepot(Calendar value) {
        this.datoForkastetAvArkivdepot = value;
    }

    /**
     * Gets the value of the tidspunktSlettetFraSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getTidspunktSlettetFraSystem() {
        return tidspunktSlettetFraSystem;
    }

    /**
     * Sets the value of the tidspunktSlettetFraSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTidspunktSlettetFraSystem(Calendar value) {
        this.tidspunktSlettetFraSystem = value;
    }

    /**
     * Gets the value of the overførtTilNyAvlevering property.
     * 
     * @return
     *     possible object is
     *     {@link OverførtTilNyAvlevering }
     *     
     */
    public OverførtTilNyAvlevering getOverførtTilNyAvlevering() {
        return overførtTilNyAvlevering;
    }

    /**
     * Sets the value of the overførtTilNyAvlevering property.
     * 
     * @param value
     *     allowed object is
     *     {@link OverførtTilNyAvlevering }
     *     
     */
    public void setOverførtTilNyAvlevering(OverførtTilNyAvlevering value) {
        this.overførtTilNyAvlevering = value;
    }

}
