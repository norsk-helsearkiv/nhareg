package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisCodeDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DiagnosisCodeConverter implements DiagnosisCodeConverterInterface {
    
    @Override
    public DiagnosisCode fromDiagnosisDTO(final DiagnosisCodeDTO diagnosisCodeDTO) {
        if (diagnosisCodeDTO == null) {
            return null;
        }
        
        return new DiagnosisCode(diagnosisCodeDTO.getCode(),
                                 diagnosisCodeDTO.getCodeSystem(),
                                 diagnosisCodeDTO.getCodeSystemVersion(),
                                 diagnosisCodeDTO.getOriginalText(),
                                 diagnosisCodeDTO.getDisplayName());
    }

    @Override
    public DiagnosisCodeDTO toDiagnosisCodeDTO(final DiagnosisCode diagnosisCode) {
        if (diagnosisCode == null) {
            return null;
        }
        
        return DiagnosisCodeDTO.builder()
                               .code(diagnosisCode.getCode())
                               .codeSystem(diagnosisCode.getCodeSystem())
                               .codeSystemVersion(diagnosisCode.getCodeSystemVersion())
                               .displayName(diagnosisCode.getDisplayName())
                               .originalText(diagnosisCode.getOriginalText())
                               .build();
    }

    @Override
    public List<DiagnosisCodeDTO> toDiagnosisCodeDTOList(final List<DiagnosisCode> diagnosisCodeList) {
        return diagnosisCodeList.stream().map(this::toDiagnosisCodeDTO).collect(Collectors.toList());
    }
    
}