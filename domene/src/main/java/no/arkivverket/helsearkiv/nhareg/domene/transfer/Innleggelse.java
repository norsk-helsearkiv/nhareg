package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.StringDateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Calendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Innleggelse", propOrder = {
    "inndato",
    "utdato"
})
@Data
public class Innleggelse implements Serializable {

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(StringDateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Calendar inndato;

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(StringDateAdapter .class)
    @XmlSchemaType(name = "date")
    protected Calendar utdato;

}