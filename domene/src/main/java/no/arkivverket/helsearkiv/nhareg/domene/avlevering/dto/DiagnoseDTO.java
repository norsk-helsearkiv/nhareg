package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnoseDTO implements Serializable {

    private String uuid;

    private String diagnosedato;

    private String diagnosekodeverk;

    private String diagnosekode;

    private String diagnosetekst;

    private String oppdatertAv;

    private String oppdatertDato;

}