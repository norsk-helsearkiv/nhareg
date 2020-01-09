package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Diagnosis;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnoseDTO;

public interface DiagnosisServiceInterface {
  
    DiagnoseDTO create(final String id, final DiagnoseDTO diagnoseDTO, final String username);

    Diagnosis update(final String id, final DiagnoseDTO diagnoseDTO, final String username);
    
    boolean delete(final String id, final DiagnoseDTO diagnoseDTO, final String username);
    
}