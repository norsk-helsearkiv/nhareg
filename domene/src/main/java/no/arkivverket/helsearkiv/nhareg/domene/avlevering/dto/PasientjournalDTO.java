package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasientjournalDTO implements Serializable {
    
    private PersondataDTO persondata;

    private List<DiagnoseDTO> diagnoser;

    private String virksomhet;

    private String avtaleBeskrivelse;

    private String avleveringBeskrivelse;

    private String lagringsenhetformat;

    private String avleveringsidentifikator;

    private boolean avleveringLaast;

}
