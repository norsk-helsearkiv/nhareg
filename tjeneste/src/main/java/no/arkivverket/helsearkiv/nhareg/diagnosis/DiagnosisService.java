package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.diagnosiscode.DiagnosisCodeDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Oppdateringsinfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.Validator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordDAO;
import no.arkivverket.helsearkiv.nhareg.util.DatoValiderer;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class DiagnosisService implements DiagnosisServiceInterface {

    @Resource
    private SessionContext sessionContext;
    
    @Inject
    private MedicalRecordDAO medicalRecordDAO;
    
    @Inject
    private DiagnosisDAO diagnosisDAO;

    @Inject
    private DiagnosisCodeDAO diagnosisCodeDAO;
    
    @Override
    public DiagnoseDTO create(final String id, final DiagnoseDTO diagnoseDTO) {
        final Pasientjournal medicalRecord = medicalRecordDAO.fetchById(id);

        if (medicalRecord == null) {
            return null;
        }

        new Validator<>(DiagnoseDTO.class).validerMedException(diagnoseDTO);
        final DatoValiderer dateValidator = new DatoValiderer();
        List<Valideringsfeil> errors = dateValidator.validerDiagnose(diagnoseDTO, medicalRecord);
        validateDiagnosisCode(diagnoseDTO.getDiagnosekode());

        if (errors.size() > 0) {
            throw new ValideringsfeilException(errors);
        }

        final Oppdateringsinfo updateInfo = createUpdateInfo();
        diagnoseDTO.setOppdatertAv(updateInfo.getOppdatertAv());

        final Diagnosekode diagnosisCode = diagnosisCodeDAO.fetchById(diagnoseDTO.getDiagnosekode());
        final Diagnose diagnose = DiagnosisConverter.convertFromDiagnosisDTO(diagnoseDTO, diagnosisCode);
        diagnose.setOppdateringsinfo(updateInfo);
        diagnose.setUuid(UUID.randomUUID().toString());
        
        diagnosisDAO.create(diagnose);
        medicalRecord.getDiagnose().add(diagnose);
        medicalRecord.setOppdateringsinfo(updateInfo);

        return DiagnosisConverter.convertToDiagnosisDTO(diagnose);
    }

    @Override 
    public Diagnose update(final String id, final DiagnoseDTO diagnoseDTO) {
        final Pasientjournal pasientjournal = medicalRecordDAO.fetchById(id);
        
        if (pasientjournal == null) {
            return null; 
        }

        // Validate diagnosis
        final ArrayList<Valideringsfeil> valideringsfeil = new Validator<>(DiagnoseDTO.class).valider(diagnoseDTO);
        final DatoValiderer datoValiderer = new DatoValiderer();
        final List<Valideringsfeil> diagfeil = datoValiderer.validerDiagnose(diagnoseDTO, pasientjournal);
        
        if (diagfeil.size() > 0) {
            valideringsfeil.addAll(diagfeil);
        }

        validateDiagnosisCode(diagnoseDTO.getDiagnosekode());
        
        if (valideringsfeil.size() != 0) {
            for (Valideringsfeil feil : valideringsfeil) {
                if (feil.getAttribute().equals("diagnosedato")) {
                    feil.setAttribute("diagnosedatotab");
                }
                if (feil.getAttribute().equals("diagnosekode")) {
                    feil.setAttribute("diagnosekodetab");
                }
            }
            throw new ValideringsfeilException(valideringsfeil);
        }

        final Diagnosekode diagnosisCode = diagnosisCodeDAO.fetchById(diagnoseDTO.getDiagnosekode());
        final Diagnose diagnose = DiagnosisConverter.convertFromDiagnosisDTO(diagnoseDTO, diagnosisCode);
        diagnose.setOppdateringsinfo(createUpdateInfo());
        diagnosisDAO.update(diagnose);

        return diagnose;
    }

    @Override
    public boolean delete(final String id, final DiagnoseDTO diagnoseDTO) {
        final Pasientjournal medicalRecord = medicalRecordDAO.fetchById(id);
        final Diagnose diagnosis = diagnosisDAO.fetchById(diagnoseDTO.getUuid());
        
        if (medicalRecord == null || diagnosis == null) {
            return false;
        }

        medicalRecord.getDiagnose().remove(diagnosis);
        diagnosisDAO.delete(diagnosis.getUuid());
        medicalRecord.setOppdateringsinfo(createUpdateInfo());

        return true;
    }

    private void validateDiagnosisCode(final String diagnosisCode) {
        if (diagnosisCode != null && !diagnosisCode.isEmpty()) {
            Map<String, String> queryParameters = new HashMap<>();
            queryParameters.put("code", diagnosisCode);

            final List<Diagnose> diagnosisList = diagnosisDAO.fetchAll(queryParameters);
            // Convert to diagnosis codes.
            final List<Diagnosekode> diagnosisCodeList = diagnosisList.stream()
                                                                      .map(Diagnose::getDiagnosekode)
                                                                      .collect(Collectors.toList());
            
            // Check if the diagnosis code exists
            if (diagnosisCodeList.size() == 0) {
                ArrayList<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();
                valideringsfeil.add(new Valideringsfeil("diagnosekode", "UkjentDiagnosekode"));
                
                throw new ValideringsfeilException(valideringsfeil);
            }
        }
    }

    private Oppdateringsinfo createUpdateInfo() {
        Oppdateringsinfo oppdateringsinfo = new Oppdateringsinfo();
        oppdateringsinfo.setOppdatertAv(sessionContext.getCallerPrincipal().getName());
        oppdateringsinfo.setSistOppdatert(Calendar.getInstance());

        return oppdateringsinfo;
    }

}