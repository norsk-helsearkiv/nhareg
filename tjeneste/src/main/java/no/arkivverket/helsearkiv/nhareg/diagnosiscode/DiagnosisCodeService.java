package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiagnosisCodeService implements DiagnosisCodeServiceInterface {

    @Inject
    private DiagnosisCodeDAO diagnosisCodeDAO;
    
    @Override
    public List<Diagnosekode> getAll(final MultivaluedMap<String, String> queryParameters) {
        return diagnosisCodeDAO.fetchAll(convertToMap(queryParameters));
    }

    @Override
    public Diagnosekode create(final Diagnosekode diagnosisCode) {
        return diagnosisCodeDAO.create(diagnosisCode);
    }

    /**
     * Filter out empty entries and convert to map.
     * @param queryParameters HTTP query parameters passed to the endpoint.
     * @return HashMap all empty params removed.
     */
    private Map<String, String> convertToMap(final MultivaluedMap<String, String> queryParameters) {
        Map<String, String> mappedQueries = new HashMap<>();
        // Convert to map
        queryParameters.forEach((key, value) -> mappedQueries.put(key, value.get(0)));
        // Remove all empty entries, as well as page and size
        mappedQueries.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue().isEmpty());
        return mappedQueries;
    }

}
