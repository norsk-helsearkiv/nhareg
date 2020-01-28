package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.common.ParameterConverter;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

public class DiagnosisCodeService implements DiagnosisCodeServiceInterface {

    @Inject
    private DiagnosisCodeDAO diagnosisCodeDAO;
    
    @Override
    public List<DiagnosisCode> getAll(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedQueries = ParameterConverter.multivaluedToMap(queryParameters);
        return diagnosisCodeDAO.fetchAll(mappedQueries);
    }

    @Override
    public DiagnosisCode create(final DiagnosisCode diagnosisCode) {
        return diagnosisCodeDAO.create(diagnosisCode);
    }

}