package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicalRecordService implements MedicalRecordServiceInterface {

    private static final String SIZE = "size";
    private static final String PAGE = "page";

    @Inject
    private MedicalRecordDAO medicalRecordDAO;
    
    @Inject
    private TransferDAO transferDAO;

    @Override
    public Pasientjournal getById(final String id) {
        return medicalRecordDAO.fetchById(id);
    }

    @Override
    public ListObject getAll(final MultivaluedMap<String, String> queryParameters) {
        final List<Pasientjournal> medicalRecordList;
        int page = 0;
        int size = 0;
        
        Map<String, String> mappedQueries = mapQueryParameters(queryParameters);
        if (queryParameters.containsKey(PAGE) && queryParameters.containsKey(SIZE)) {
            page = Integer.parseInt(queryParameters.getFirst(PAGE));
            size = Integer.parseInt(queryParameters.getFirst(SIZE));
            medicalRecordList = medicalRecordDAO.fetchAllPaged(mappedQueries, page, size);
        } else {
            medicalRecordList = medicalRecordDAO.fetchAll(mappedQueries);
        }

        List<PasientjournalSokeresultatDTO> medicalRecordSearchResultList = medicalRecordMapper
            .mapToSearchResultDTOList(medicalRecordList);
        // TODO SORT RESULTS
        return new ListObject<>(medicalRecordSearchResultList, medicalRecordSearchResultList.size(), page, size);
    }

    @Override
    public ListObject getAllWithTransfers(final MultivaluedMap<String, String> queryParameters) {
        int page = 0;
        int size  = 0;

        if (queryParameters.containsKey(PAGE) && queryParameters.containsKey(SIZE)) {
            page = Integer.parseInt(queryParameters.getFirst(PAGE));
            size = Integer.parseInt(queryParameters.getFirst(SIZE));
        }

        final Map<String, String> mappedQueries = convertToMap(queryParameters);
        final List<RecordTransferDTO> recordTransferDTOList = 
            medicalRecordDAO.fetchAllRecordTransfers(mappedQueries, page, size);
        return new ListObject<>(recordTransferDTOList, recordTransferDTOList.size(), page, size);
    }

    @Override
    public String getTransferIdFromMedicalRecord(final String id) {
        return String.valueOf(medicalRecordDAO.fetchTransferIdFromMedicalRecord(id));
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
        mappedQueries.entrySet().removeIf(entry ->
                                              entry.getValue() == null ||
                                              entry.getValue().isEmpty() ||
                                              SIZE.equals(entry.getKey()) ||
                                              PAGE.equals(entry.getKey()));
        return mappedQueries;
    }
    
}