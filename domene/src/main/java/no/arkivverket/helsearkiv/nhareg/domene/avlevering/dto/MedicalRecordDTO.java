package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordDTO {
    
    private PersondataDTO persondata;

    private List<DiagnoseDTO> diagnoser;
 
    private String virksomhet;

    private String avtaleBeskrivelse;

    private String avleveringBeskrivelse;

    private String avleveringsidentifikator;

    private String lagringsenhetformat;

    private boolean avleveringLaast;

}