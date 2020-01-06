package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.DagEllerAar;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnoseDTO implements Serializable {

    private String uuid;

    @NotNull
    @Size(min = 1)
    @DagEllerAar
    private String diagnosedato;

    private String diagnosekodeverk;
    
    private String diagnosekode;

    @NotNull
    @Size(min = 1)
    private String diagnosetekst;

    private String oppdatertAv;

    private String oppdatertDato;

}