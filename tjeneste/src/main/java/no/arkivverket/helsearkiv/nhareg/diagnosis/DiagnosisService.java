package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.diagnosiscode.DiagnosisCodeDAO;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Diagnosis;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.UpdateInfo;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.Validator;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordDAO;
import no.arkivverket.helsearkiv.nhareg.validation.DateValidation;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiagnosisService implements DiagnosisServiceInterface {
    
    @Inject
    private MedicalRecordDAO medicalRecordDAO;
    
    @Inject
    private DiagnosisDAO diagnosisDAO;

    @Inject
    private DiagnosisCodeDAO diagnosisCodeDAO;
    
    @Inject
    private DiagnosisConverterInterface diagnosisConverter;
    
    @Override
    public DiagnoseDTO create(final String id, final DiagnoseDTO diagnoseDTO, final String username) {
        final MedicalRecord medicalRecord = medicalRecordDAO.fetchById(id);

        if (medicalRecord == null) {
            return null;
        }

        new Validator<>(DiagnoseDTO.class).validateWithException(diagnoseDTO);
        final DateValidation dateValidator = new DateValidation();
        final List<ValidationError> errors = dateValidator.validateDiagnosis(diagnoseDTO, medicalRecord);
        validateDiagnosisCode(diagnoseDTO.getDiagnosisCode());

        if (errors.size() > 0) {
            throw new ValidationErrorException(errors);
        }

        final UpdateInfo updateInfo = createUpdateInfo(username);
        diagnoseDTO.setUpdatedBy(updateInfo.getUpdatedBy());

        final DiagnosisCode diagnosisCode = diagnosisCodeDAO.fetchById(diagnoseDTO.getDiagnosisCode());
        final Diagnosis diagnosis = diagnosisConverter.fromDiagnosisDTO(diagnoseDTO, diagnosisCode);
        diagnosis.setUpdateInfo(updateInfo);
        diagnosis.setUuid(UUID.randomUUID().toString());
        
        diagnosisDAO.create(diagnosis);
        medicalRecord.getDiagnosis().add(diagnosis);
        medicalRecord.setUpdateInfo(updateInfo);

        return diagnosisConverter.toDiagnosisDTO(diagnosis);
    }

    @Override 
    public Diagnosis update(final String id, final DiagnoseDTO diagnoseDTO, final String username) {
        final MedicalRecord medicalRecord = medicalRecordDAO.fetchById(id);
        
        if (medicalRecord == null) {
            return null; 
        }

        // Validate diagnosis
        final List<ValidationError> diagnosisValidationList = new Validator<>(DiagnoseDTO.class).validate(diagnoseDTO);
        final DateValidation dateValidation = new DateValidation();
        final List<ValidationError> dateValidationList = dateValidation.validateDiagnosis(diagnoseDTO, medicalRecord);
        
        if (dateValidationList.size() > 0) {
            diagnosisValidationList.addAll(dateValidationList);
        }

        validateDiagnosisCode(diagnoseDTO.getDiagnosisCode());
        
        if (diagnosisValidationList.size() != 0) {
            for (ValidationError feil : diagnosisValidationList) {
                if (feil.getAttribute().equals("diagnosedato")) {
                    feil.setAttribute("diagnosedatotab");
                }
                if (feil.getAttribute().equals("diagnosisCode")) {
                    feil.setAttribute("diagnosekodetab");
                }
            }
            throw new ValidationErrorException(diagnosisValidationList);
        }

        final DiagnosisCode diagnosisCode = diagnosisCodeDAO.fetchById(diagnoseDTO.getDiagnosisCode());
        final Diagnosis diagnosis = diagnosisConverter.fromDiagnosisDTO(diagnoseDTO, diagnosisCode);
        diagnosis.setUpdateInfo(createUpdateInfo(username));
        diagnosisDAO.update(diagnosis);

        return diagnosis;
    }

    @Override
    public boolean delete(final String id, final DiagnoseDTO diagnoseDTO, final String username) {
        final MedicalRecord medicalRecord = medicalRecordDAO.fetchById(id);
        final Diagnosis diagnosis = diagnosisDAO.fetchById(diagnoseDTO.getUuid());
        
        if (medicalRecord == null || diagnosis == null) {
            return false;
        }

        medicalRecord.getDiagnosis().remove(diagnosis);
        diagnosisDAO.delete(diagnosis.getUuid());
        medicalRecord.setUpdateInfo(createUpdateInfo(username));

        return true;
    }

    private void validateDiagnosisCode(final String diagnosisCode) {
        if (diagnosisCode != null && !diagnosisCode.isEmpty()) {
            final DiagnosisCode existingDiagnosisCode = diagnosisCodeDAO.fetchById(diagnosisCode);
            
            if (existingDiagnosisCode == null) {
                final ArrayList<ValidationError> validationError = new ArrayList<>();
                validationError.add(new ValidationError("diagnosekode", "UkjentDiagnosekode"));

                throw new ValidationErrorException(validationError);
            }
        }
    }

    private UpdateInfo createUpdateInfo(final String username) {
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setUpdatedBy(username);
        updateInfo.setLastUpdated(LocalDateTime.now());

        return updateInfo;
    }

}