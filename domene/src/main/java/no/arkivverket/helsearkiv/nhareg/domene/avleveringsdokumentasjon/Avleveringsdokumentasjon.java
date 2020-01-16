package no.arkivverket.helsearkiv.nhareg.domene.avleveringsdokumentasjon;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * En instans av denne klassen benyttes for å registrere opplysninger om den produksjon av arkivuttrekk for et sett
 * enkeltpasienter og overføring disse til arkivdepot som blir foretatt på grunnlag av en avtale om avlevering
 * inngått mellom arkivskaper og arkivdepot.  
 *             
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Avleveringsdokumentasjon", propOrder = {
    "avleveringsoversikt",
    "overf\u00f8ringTilArkivdepot",
    "oppdateringsinfo",
    "avleveringsbeskrivelse"
})
public class Avleveringsdokumentasjon implements Serializable {

    @XmlElement(name = "Avleveringsoversikt", namespace = "http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon")
    protected List<Avleveringsoversikt> avleveringsoversikt;
   
    @XmlElement(name = "Overf\u00f8ring_til_arkivdepot", namespace = "http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon")
    protected OverføringTilArkivdepot overføringTilArkivdepot;
   
    @XmlElement(name = "Oppdateringsinfo", namespace = "http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon", required = true)
    protected Oppdateringsinfo oppdateringsinfo;
   
    @XmlElement(name = "Avleveringsbeskrivelse", namespace = "http://eisi.helsedir.no/anno2014/nha-1.61-RGM/avlevering/Avleveringsdokumentasjon", required = true)
    protected Avleveringsbeskrivelse avleveringsbeskrivelse;

    public List<Avleveringsoversikt> getAvleveringsoversikt() {
        if (avleveringsoversikt == null) {
            avleveringsoversikt = new ArrayList<Avleveringsoversikt>();
        }
        return this.avleveringsoversikt;
    }
    
}