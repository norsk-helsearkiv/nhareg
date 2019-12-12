package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DiagnosisMapper {
    
    public static DiagnoseDTO mapToDiagnosisDTO(final Diagnose diagnosis) {
        if (diagnosis == null) {
            return null;
        }
        
        final String uuid = diagnosis.getUuid();
        final String diagnosisDate = diagnosis.getDiagdato().getStringValue();
        
        final Diagnosekode diagnosisCode = diagnosis.getDiagnosekode();
        final String diagnosisCodeString = diagnosisCode != null ? diagnosisCode.getCode() : null;
        final String diagnosisCodingSystem = diagnosisCode != null ? diagnosisCode.getCodeSystem() : null;
        final String diagnosisText = diagnosis.getDiagnosetekst();
        final String updatedBy = diagnosis.getOppdateringsinfo().getOppdatertAv();
        final Date date = diagnosis.getOppdateringsinfo().getSistOppdatert().getTime();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final String updatedDate = GyldigeDatoformater.asLocalDate(date).format(formatter);

        return new DiagnoseDTO(uuid, diagnosisDate, diagnosisCodingSystem, diagnosisCodeString, diagnosisText, 
                               updatedBy, updatedDate);
    }
    
    public static List<DiagnoseDTO> mapToDiagnosisDTOList(final Collection<Diagnose> diagnosisList) {
        return diagnosisList.stream()
            .map(DiagnosisMapper::mapToDiagnosisDTO)
            .collect(Collectors.toList());
    }
}