package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverterInterface;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DateOrYear;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Diagnosis;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class DiagnosisConverter implements DiagnosisConverterInterface {

    private DateOrYearConverterInterface dateOrYearConverter = new DateOrYearConverter();
    
    @Override
    public Diagnosis fromDiagnosisDTO(final DiagnosisDTO diagnosisDTO, final DiagnosisCode diagnosisCode) {
        if (diagnosisDTO == null || diagnosisCode == null) {
            return null;
        }
        
        final String diagnosisDateString = diagnosisDTO.getDiagnosisDate();
        final DateOrYear diagnosisDate = dateOrYearConverter.toDateOrYear(diagnosisDateString);

        return new Diagnosis(diagnosisDTO.getUuid(), diagnosisDate, diagnosisCode.getCode(), 
                             diagnosisDTO.getDiagnosisText(), diagnosisCode.getCodeSystem(),    
                             diagnosisCode.getCodeSystemVersion(), null);
    }
    
    @Override 
    public DiagnosisDTO toDiagnosisDTO(final Diagnosis diagnosis) {
        if (diagnosis == null) {
            return null;
        }
        
        final String uuid = diagnosis.getUuid();
        final String diagnosisDate = dateOrYearConverter.fromDateOrYear(diagnosis.getDiagnosisDate());
        final String diagnosisCode = diagnosis.getDiagnosisCode();
        final String diagnosisCodingSystem = diagnosis.getDiagnosisCodingSystem();
        final String diagnosisText = diagnosis.getDiagnosisText();
        final String updatedBy = diagnosis.getUpdateInfo().getUpdatedBy();
        final LocalDateTime lastUpdated = diagnosis.getUpdateInfo().getLastUpdated();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        final String updatedDate = lastUpdated.format(formatter);

        return new DiagnosisDTO(uuid, diagnosisDate, diagnosisCodingSystem, diagnosisCode, diagnosisText,
                                updatedBy, updatedDate);
    }
    
    @Override 
    public Set<DiagnosisDTO> toDiagnosisDTOSet(final Collection<Diagnosis> diagnosisList) {
        if (diagnosisList == null) {
            return null;
        }
        
        return diagnosisList.stream().map(this::toDiagnosisDTO).collect(Collectors.toSet());
    }
}