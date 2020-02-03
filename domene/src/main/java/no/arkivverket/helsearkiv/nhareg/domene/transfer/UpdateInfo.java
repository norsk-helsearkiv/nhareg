package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateTimeConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UpdateInfo implements Serializable {
    
    @Column(name = "sistOppdatert")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime lastUpdated;

    @Column(name = "oppdatertAv")
    private String updatedBy;

    @Column(name = "prosesstrinn")
    private String processSteps;

}