package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverterInterface;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DateOrYear;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Diagnosis;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.UpdateInfo;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class DiagnosisConverter implements DiagnosisConverterInterface {

    private final DateOrYearConverterInterface dateOrYearConverter = new DateOrYearConverter();
    
    @Override
    public Diagnosis fromDiagnosisDTO(final DiagnosisDTO diagnosisDTO, final DiagnosisCode diagnosisCode) {
        if (diagnosisDTO == null) {
            return null;
        }
        
        final String diagnosisDateString = diagnosisDTO.getDiagnosisDate();
        final DateOrYear diagnosisDate = dateOrYearConverter.toDateOrYear(diagnosisDateString);
        final String code = diagnosisCode == null ? null : diagnosisCode.getCode();
        final String codeSystem = diagnosisCode == null ? null : diagnosisCode.getCodeSystem();
        final String codeSystemVersion = diagnosisCode == null ? null : diagnosisCode.getCodeSystemVersion();

        return Diagnosis.builder()
                        .uuid(diagnosisDTO.getUuid())
                        .diagnosisDate(diagnosisDate)
                        .diagnosisCode(code)
                        .diagnosisText(diagnosisDTO.getDiagnosisText())
                        .diagnosisCodingSystem(codeSystem)
                        .diagnosisCodingSystemVersion(codeSystemVersion)
                        .build();
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
        final String diagnosisCodeSystemVersion = diagnosis.getDiagnosisCodingSystemVersion();
        final String diagnosisText = diagnosis.getDiagnosisText();
        final UpdateInfo updateInfo = diagnosis.getUpdateInfo();
        String updatedDate = null;
        String updatedBy = null;
        
        if (updateInfo != null) {
            final LocalDateTime lastUpdated = updateInfo.getLastUpdated();
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
            updatedBy = updateInfo.getUpdatedBy();
            updatedDate = lastUpdated.format(formatter);
        }

        return DiagnosisDTO.builder()
                           .uuid(uuid)
                           .diagnosisDate(diagnosisDate)
                           .diagnosisCodeSystem(diagnosisCodingSystem)
                           .diagnosisCode(diagnosisCode)
                           .diagnosisText(diagnosisText)
                           .diagnosisCodeSystemVersion(diagnosisCodeSystemVersion)
                           .updatedBy(updatedBy)
                           .updatedDate(updatedDate)
                           .build();
    }
    
    @Override 
    public Set<DiagnosisDTO> toDiagnosisDTOSet(final Collection<Diagnosis> diagnosisList) {
        if (diagnosisList == null) {
            return null;
        }
        
        return diagnosisList.stream().map(this::toDiagnosisDTO).collect(Collectors.toSet());
    }
}