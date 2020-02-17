package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateConverter;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diagnosekodeverk")
public class DiagnosisCodeSystem {
    
    @Id
    @Column(name = "kodeverkversjon")
    private String code;

    @Convert(converter = LocalDateConverter.class)
    @Column(name = "gyldig_fra_dato")
    private LocalDate validFromDate;

    @Convert(converter = LocalDateConverter.class)
    @Column(name = "gyldig_til_dato")
    private LocalDate validToDate;
    
}