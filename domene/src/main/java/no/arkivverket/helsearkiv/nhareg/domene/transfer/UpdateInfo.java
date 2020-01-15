package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Calendar;

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