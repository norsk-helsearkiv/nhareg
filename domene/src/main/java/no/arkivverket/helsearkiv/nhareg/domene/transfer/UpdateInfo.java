package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateTimeConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Embeddable
public class UpdateInfo implements Serializable {
    
    @Column(name = "sistOppdatert")
    @Convert(converter = LocalDateTimeConverter.class)
    protected LocalDateTime lastUpdated;

    @Column(name = "oppdatertAv")
    protected String updatedBy;

    @Column(name = "prosesstrinn")
    protected String processSteps;

}