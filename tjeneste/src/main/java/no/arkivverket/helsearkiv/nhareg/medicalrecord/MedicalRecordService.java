package no.arkivverket.helsearkiv.nhareg.medicalrecord;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;


public class MedicalRecordService implements MedicalRecordServiceInterface {

    private static final String NUMBER = "antall";
    private static final String PAGE = "side";

    @Inject
    private MedicalRecordDAO medicalRecordDAO;
    
    @Inject
    private MedicalRecordMapper medicalRecordMapper;

    @Override
    public Pasientjournal getById(final String id) {
        return medicalRecordDAO.getById(id);
    }

    @Override
    public ListObject getAll(final MultivaluedMap<String, String> queryParameters) {
        List<Pasientjournal> medicalRecordList;
        int page = 0;
        int number = 0;
        
        Map<String, String> mappedQueries = mapQueryParameters(queryParameters);
        if (queryParameters.containsKey(PAGE) && queryParameters.containsKey(NUMBER)) {
            page = Integer.parseInt(queryParameters.getFirst(PAGE));
            number = Integer.parseInt(queryParameters.getFirst(NUMBER));
            medicalRecordList = medicalRecordDAO.getAllPaged(mappedQueries, page, number);
        } else {
            medicalRecordList = medicalRecordDAO.getAll(mappedQueries);
        }

        List<PasientjournalSokeresultatDTO> medicalRecordSearchResultList = medicalRecordMapper
            .mapToSearchResultDTOList(medicalRecordList);
        // TODO SORT RESULTS
        return new ListObject<>(medicalRecordSearchResultList, medicalRecordSearchResultList.size(), page, number);
    }

    @Override
    public String getDeliveryIdFromMedicalRecord(final String id) {
        return String.valueOf(medicalRecordDAO.getDeliveryIdFromMedicalRecord(id));
    }
    
    /**
     * Map the query parameters to names that can be used when querying. Also converts from MultivaluedMap to HashMap.
     * @param queryParameters HTTP query parameters passed to the endpoint.
     * @return HashMap with filter parameters from the query parameters.
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

        return mappedQueryParameters;
    }
}

