package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.StringDateTimeAdapter;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Calendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Oppdateringsinfo", propOrder = {
    "sistOppdatert",
    "oppdatertAv",
    "prosesstrinn"
})
@Data
@Embeddable
public class Oppdateringsinfo implements Serializable {
    
    @XmlElement(name = "sist_oppdatert", required = true, type = String.class)
    @XmlJavaTypeAdapter(StringDateTimeAdapter.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar sistOppdatert;

    @XmlElement(name = "oppdatert_av", required = true)
    protected String oppdatertAv;

    @XmlElement(required = true)
    protected String prosesstrinn;

}
