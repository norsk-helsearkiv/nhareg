package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverterInterface;
import no.arkivverket.helsearkiv.nhareg.domene.common.ValidDateFormats;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DateOrYear;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Diagnosis;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnoseDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DiagnosisConverter implements DiagnosisConverterInterface {

    private DateOrYearConverterInterface dateOrYearConverter = new DateOrYearConverter();
    
    @Override
    public Diagnosis fromDiagnosisDTO(final DiagnoseDTO diagnosisDTO, final DiagnosisCode diagnosisCode) {
        if (diagnosisDTO == null) {
            return null;
        }
        
        final String diagnosisDateString = diagnosisDTO.getDiagnosisDate();
        final DateOrYear diagnosisDate = dateOrYearConverter.toDateOrYear(diagnosisDateString);

        return new Diagnosis(diagnosisDTO.getUuid(), diagnosisDate, diagnosisCode, diagnosisDTO.getDiagnosisText(), null);
    }
    
    @Override 
    public DiagnoseDTO toDiagnosisDTO(final Diagnosis diagnosis) {
        if (diagnosis == null) {
            return null;
        }
        
        final String uuid = diagnosis.getUuid();
        final String diagnosisDate = dateOrYearConverter.fromDateOrYear(diagnosis.getDiagnosisDate());
        final DiagnosisCode diagnosisCode = diagnosis.getDiagnosisCode();
        final String diagnosisCodeString = diagnosisCode != null ? diagnosisCode.getCode() : null;
        final String diagnosisCodingSystem = diagnosisCode != null ? diagnosisCode.getCodeSystemVersion() : null;
        final String diagnosisText = diagnosis.getDiagnosisText();
        final String updatedBy = diagnosis.getUpdateInfo().getUpdatedBy();
        final Date date = diagnosis.getUpdateInfo().getLastUpdated().getTime();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime localDate = ValidDateFormats.asLocalDateTime(date);
        final String updatedDate = localDate.format(formatter);

        return new DiagnoseDTO(uuid, diagnosisDate, diagnosisCodingSystem, diagnosisCodeString, diagnosisText, 
                               updatedBy, updatedDate);
    }
    
    @Override 
    public List<DiagnoseDTO> toDiagnosisDTOList(final Collection<Diagnosis> diagnosisList) {
        if (diagnosisList == null) {
            return null;
        }
        
        return diagnosisList.stream().map(this::toDiagnosisDTO).collect(Collectors.toList());
    }
}