package no.arkivverket.helsearkiv.nhareg.medicalrecord;


import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
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
    private MedicalRecordMapper medicalRecordMapper;

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
        final List<RecordTransferDTO> recordTransferDTOList;
        int page = 0;
        int size  = 0;
        
        Map<String, String> mappedQueries = mapQueryParameters(queryParameters);
        if (queryParameters.containsKey(PAGE) && queryParameters.containsKey(SIZE)) {
            page = Integer.parseInt(queryParameters.getFirst(PAGE));
            size = Integer.parseInt(queryParameters.getFirst(SIZE));
        }
        
        recordTransferDTOList = medicalRecordDAO.fetchAllRecordTransfers(mappedQueries, page, size);
        return new ListObject<>(recordTransferDTOList, recordTransferDTOList.size(), page, size);
    }

    @Override
    public String getTransferIdFromMedicalRecord(final String id) {
        return String.valueOf(medicalRecordDAO.fetchTransferIdFromMedicalRecord(id));
    }

    /**
     * Map the query parameters to names that can be used when querying. Also converts from MultivaluedMap to HashMap.
     * @param queryParameters HTTP query parameters passed to the endpoint.
     * @return HashMap with filter parameters from the query parameters, with all empty params removed.
     */
    private Map<String, String> mapQueryParameters(final MultivaluedMap<String, String> queryParameters) {
        Map<String, String> mappedQueryParameters = new HashMap<String, String>();
        
        mappedQueryParameters.put("fanearkid", queryParameters.getFirst("sokFanearkId"));
        mappedQueryParameters.put("lagringsenhet", queryParameters.getFirst("sokLagringsenhet"));
        mappedQueryParameters.put("fodselsnummer", queryParameters.getFirst("sokFodselsnummer"));
        mappedQueryParameters.put("navn", queryParameters.getFirst("sokNavn"));
        mappedQueryParameters.put("fodt", queryParameters.getFirst("sokFodt"));
        mappedQueryParameters.put("oppdatertAv", queryParameters.getFirst("sokOppdatertAv"));
        mappedQueryParameters.put("sistOppdatert", queryParameters.getFirst("sokSistOppdatert"));

        mappedQueryParameters.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        return mappedQueryParameters;
    }
}

