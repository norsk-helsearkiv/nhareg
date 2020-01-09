package no.arkivverket.helsearkiv.nhareg.domene.avleveringsdokumentasjon;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.StringDateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Calendar;

/**
 * 
 * En instans av denne klassen benyttes for å registrere opplysninger om en avtale om avlevering av materiale fra et
 * pasientarkiv som er inngått mellom arkivskaper og arkivdepot.
 *
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "avleveringsidentifikator",
    "arkivskaperID",
    "arkivID",
    "avtaledato",
    "datoForPasientutvelgelse",
    "planlagtOppstart",
    "planlagtAvsluttet",
    "utfyllendeOpplysninger"
})
@XmlRootElement(name = "Avleveringsbeskrivelse")
public class Avleveringsbeskrivelse implements Serializable {
    @XmlElement(required = true)
    protected String avleveringsidentifikator;

    @XmlElement(name = "arkivskaper_ID", required = true)
    protected String arkivskaperID;

    @XmlElement(name = "arkiv_ID", required = true)
    protected String arkivID;

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(StringDateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Calendar avtaledato;

    @XmlElement(name = "dato_for_pasientutvelgelse", required = true, type = String.class)
    @XmlJavaTypeAdapter(StringDateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Calendar datoForPasientutvelgelse;

    @XmlElement(name = "planlagt_oppstart", required = true, type = String.class)
    @XmlJavaTypeAdapter(StringDateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Calendar planlagtOppstart;

    @XmlElement(name = "planlagt_avsluttet", required = true, type = String.class)
    @XmlJavaTypeAdapter(StringDateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Calendar planlagtAvsluttet;

    @XmlElement(name = "utfyllende_opplysninger")
    protected String utfyllendeOpplysninger;

}