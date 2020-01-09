package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DateOrYear;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosis;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DiagnosisCode;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.felles.ValidDateFormats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DiagnosisConverter {

    public static Diagnosis convertFromDiagnosisDTO(final DiagnoseDTO diagnosisDTO, final DiagnosisCode diagnosisCode) {
        final String diagnosisDateString = diagnosisDTO.getDiagnosisDate();
        final DateOrYear diagnosisDate = DateOrYearConverter.toDateOrYear(diagnosisDateString);

        return new Diagnosis(diagnosisDTO.getUuid(), diagnosisDate, diagnosisCode, diagnosisDTO.getDiagnosisText(), null);
    }
    
    public static DiagnoseDTO convertToDiagnosisDTO(final Diagnosis diagnosis) {
        if (diagnosis == null) {
            return null;
        }
        
        final String uuid = diagnosis.getUuid();
        final String diagnosisDate = diagnosis.getDiagdato().getStringValue();
        final DiagnosisCode diagnosisCode = diagnosis.getDiagnosisCode();
        final String diagnosisCodeString = diagnosisCode != null ? diagnosisCode.getCode() : null;
        final String diagnosisCodingSystem = diagnosisCode != null ? diagnosisCode.getCodeSystemVersion() : null;
        final String diagnosisText = diagnosis.getDiagnosetekst();
        final String updatedBy = diagnosis.getOppdateringsinfo().getOppdatertAv();
        final Date date = diagnosis.getOppdateringsinfo().getSistOppdatert().getTime();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime localDate = ValidDateFormats.asLocalDateTime(date);
        final String updatedDate = localDate.format(formatter);

        return new DiagnoseDTO(uuid, diagnosisDate, diagnosisCodingSystem, diagnosisCodeString, diagnosisText, 
                               updatedBy, updatedDate);
    }
    
    public static List<DiagnoseDTO> convertToDiagnosisDTOList(final Collection<Diagnosis> diagnosisList) {
        return diagnosisList.stream()
            .map(DiagnosisConverter::convertToDiagnosisDTO)
            .collect(Collectors.toList());
    }
}