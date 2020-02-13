package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.common.ParameterConverter;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisCodeDTO;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

import static no.arkivverket.helsearkiv.nhareg.common.ParameterConverter.multivaluedToMap;

public class DiagnosisCodeService implements DiagnosisCodeServiceInterface {

    @Inject
    private DiagnosisCodeDAO diagnosisCodeDAO;
    
    @Inject
    private DiagnosisCodeConverterInterface diagnosisCodeConverter;
    
    @Override
    public List<DiagnosisCodeDTO> getAll(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedQueries = multivaluedToMap(queryParameters);
        final List<DiagnosisCode> diagnosisCodeList = diagnosisCodeDAO.fetchAll(mappedQueries);
        
        return diagnosisCodeConverter.toDiagnosisCodeDTOList(diagnosisCodeList);
    }

    @Override
    public DiagnosisCodeDTO create(final DiagnosisCodeDTO diagnosisCodeDTO) {
        final DiagnosisCode diagnosisCode = diagnosisCodeConverter.fromDiagnosisDTO(diagnosisCodeDTO);
        final DiagnosisCode newDiagnosisCode = diagnosisCodeDAO.create(diagnosisCode);

        return diagnosisCodeConverter.toDiagnosisCodeDTO(newDiagnosisCode);
    }

    @Override
    public List<DiagnosisCodeDTO> getAllByCode(final String code, final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> parameters = ParameterConverter.multivaluedToMap(queryParameters);
        final List<DiagnosisCode> diagnosisCodeList = diagnosisCodeDAO.fetchAllByCode(code, parameters);
        
        return diagnosisCodeConverter.toDiagnosisCodeDTOList(diagnosisCodeList);
    }

}