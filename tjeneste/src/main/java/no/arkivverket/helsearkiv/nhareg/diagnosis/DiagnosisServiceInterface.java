package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Diagnosis;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisDTO;

public interface DiagnosisServiceInterface {
  
    DiagnosisDTO create(final String id, final DiagnosisDTO diagnosisDTO, final String username);

    Diagnosis update(final String id, final DiagnosisDTO diagnosisDTO, final String username);
    
    boolean delete(final String id, final DiagnosisDTO diagnosisDTO, final String username);
    
}