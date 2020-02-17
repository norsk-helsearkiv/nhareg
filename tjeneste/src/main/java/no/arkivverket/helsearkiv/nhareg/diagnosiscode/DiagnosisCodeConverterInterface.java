package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisCodeDTO;

import java.util.List;

public interface DiagnosisCodeConverterInterface {
    
    DiagnosisCode fromDiagnosisDTO(final DiagnosisCodeDTO diagnosisCodeDTO);

    DiagnosisCodeDTO toDiagnosisCodeDTO(final DiagnosisCode diagnosisCode);
    
    List<DiagnosisCodeDTO> toDiagnosisCodeDTOList(final List<DiagnosisCode> diagnosisCodeList);
    
}