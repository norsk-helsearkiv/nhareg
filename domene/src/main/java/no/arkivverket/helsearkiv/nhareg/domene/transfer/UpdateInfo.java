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
public class UpdateInfo implements Serializable {
    
    @Column(name = "sistOppdatert")
    protected Calendar lastUpdated;

    @Column(name = "oppdatertAv")
    protected String updatedBy;

    @Column(name = "prosesstrinn")
    protected String processSteps;

}