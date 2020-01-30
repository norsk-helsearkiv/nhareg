package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Diagnosis;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisDTO;

import java.util.Collection;
import java.util.Set;

public interface DiagnosisConverterInterface {
    
    Diagnosis fromDiagnosisDTO(final DiagnosisDTO diagnosisDTO, final DiagnosisCode diagnosisCode);

    DiagnosisDTO toDiagnosisDTO(final Diagnosis diagnosis);

    Set<DiagnosisDTO> toDiagnosisDTOSet(final Collection<Diagnosis> diagnosisList);
    
}