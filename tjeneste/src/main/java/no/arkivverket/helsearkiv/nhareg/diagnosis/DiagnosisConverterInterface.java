package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Diagnosis;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnoseDTO;

import java.util.Collection;
import java.util.List;

public interface DiagnosisConverterInterface {
    
    Diagnosis fromDiagnosisDTO(DiagnoseDTO diagnosisDTO, DiagnosisCode diagnosisCode);

    DiagnoseDTO toDiagnosisDTO(Diagnosis diagnosis);

    List<DiagnoseDTO> toDiagnosisDTOList(Collection<Diagnosis> diagnosisList);
    
}