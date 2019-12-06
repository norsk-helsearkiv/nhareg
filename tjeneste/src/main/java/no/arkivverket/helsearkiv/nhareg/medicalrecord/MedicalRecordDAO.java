package no.arkivverket.helsearkiv.nhareg.medicalrecord;


import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.*;


@Stateless
public class MedicalRecordDAO extends EntityDAO<Pasientjournal> {
    
    private final static String GET_RECORD_TRANSFER_RESULTS = "SELECT uuid, fanearkid, daar, faar, pid, pnavn," 
                                                              + "journalnummer, lopenummer, ps.oppdatertAv, " 
                                                              + "opprettetDato, avleveringsidentifikator, " 
                                                              + "lagringsenhetformat, laast "
                                                              + "FROM pasientjournal ps "
                                                              + "JOIN avlevering_pasientjournal aps "
                                                              + "ON ps.uuid = aps.pasientjournal_uuid "
                                                              + "JOIN avlevering a "
                                                              + "ON a.avleveringsidentifikator = aps.Avlevering_avleveringsidentifikator";
    
    public MedicalRecordDAO() {
        super(Pasientjournal.class, "uuid");
    }

    @Override
    public List<Pasientjournal> fetchAll(final Map<String, String> queryParameters) {
        return fetchAllPaged(queryParameters, 0, 0);
    }

    @Override
    public List<Pasientjournal> fetchAllPaged(final Map<String, String> queryParameters, final int page, final int size) {
        List<Pasientjournal> pagedResultList = super.fetchAllPaged(queryParameters, page, size);        
        // TODO sokestring query results
        forceLoadStorageUnits(pagedResultList);
        return pagedResultList;
    }
    
    public List<RecordTransferDTO> fetchAllRecordTransfers(final Map<String, String> queryParameters, 
                                                           final int page, 
                                                           final int size) {
        final EntityManager entityManager = getEntityManager();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Pasientjournal> criteriaQuery = criteriaBuilder.createQuery(Pasientjournal.class);
        final Root<Pasientjournal> root = criteriaQuery.from(Pasientjournal.class);
        final Predicate[] predicates = extractPredicates(queryParameters, criteriaBuilder, root);
        
        // TODO fix predicates
        Query query = entityManager.createNativeQuery(GET_RECORD_TRANSFER_RESULTS);

        if (page > 0 && size > 0) {
            query.setFirstResult(page);
            query.setMaxResults(size);
        }
        
        List<Object[]> queryResults = query.getResultList();
        List<RecordTransferDTO> recordTransferDTOList = new ArrayList<>();
        
        if (queryResults == null) {
            return null;
        }
        
        // Map each row into a RecordTransferDTO object and add it to a list.
        queryResults.forEach(objects -> recordTransferDTOList.add(mapFromObjectsToRecordTransferDTO(objects)));
        
        return recordTransferDTOList;
    }

    /**
     * Get the transfer id (avleverings id) associated with the medical record (pasientjournal) id.
     * @param id Medical record id (pasientjournal id)
     * @return the delivery id as an int
     */
    public int fetchTransferIdFromMedicalRecord(final String id) {
        Query query = this.getEntityManager().createNativeQuery("SELECT Avlevering_avleveringsidentifikator "
                                                                + "FROM Avlevering_Pasientjournal "
                                                                + "WHERE pasientjournal_uuid=?");
        query.setParameter(1, id);
        return query.getFirstResult();
    }
    
    @Override
    protected Predicate[] extractPredicates(final Map<String, String> queryParameters,
                                            final CriteriaBuilder criteriaBuilder, 
                                            final Root<Pasientjournal> root) {
        final List<Predicate> predicates = new ArrayList<>();
        final HashMap<String, Path> predicateMap = new HashMap<String, Path>() {{
            put("fanearkid",     root.get("fanearkid"));
            put("lagringsenhet", root.join("lagringsenhet").get("identifikator"));
            put("fodselsnummer", root.get("grunnopplysninger").get("identifikator").get("pid"));
            put("navn",          root.get("grunnopplysninger").get("pnavn"));
            put("oppdatertAv",   root.get("oppdateringsinfo").get("oppdatertAv"));
        }};
        
        for (String key: predicateMap.keySet()) {
            String value = queryParameters.get(key);
            if (!value.isEmpty()) {
                final Path path = predicateMap.get(key);
                value = "%" + value.replace(' ', '%') + "%";
                predicates.add(criteriaBuilder.like(path, value));
            }
        }
      
        String key = "sistOppdatert";
        String value = queryParameters.get(key);
        if (!value.isEmpty()) {
            final Path<Date> path = root.get("oppdateringsinfo").get("sistOppdatert");
            // Deal with only year specified.
            if (value.length() == 4) {
                final LocalDate date = GyldigeDatoformater.getLocalDate(value);
                final Predicate yearPredicate = criteriaBuilder.greaterThanOrEqualTo(path, GyldigeDatoformater.asLegacyDate(date));
                final LocalDate nextYear = date != null ? date.plusYears(1) : null;
                final Predicate nextYearPredicate = criteriaBuilder.lessThan(path, GyldigeDatoformater.asLegacyDate(nextYear));
                predicates.add(yearPredicate);
                predicates.add(nextYearPredicate);
            } else {
                final List<Predicate> datePredicates = createDatePredicates(criteriaBuilder, path, value);
                predicates.addAll(datePredicates);
            }
        }
        
        key = "fodt";
        value = queryParameters.get(key);
        if (!value.isEmpty()) {
            // Check if its only the year specified
            if (value.length() == 4) {
                Path<Integer> path = root.get("grunnopplysninger").get("født").get("aar");
                int year = Integer.parseInt(value);
                final Predicate yearPredicate = criteriaBuilder.greaterThanOrEqualTo(path, year);
                final Predicate nextYearPredicate = criteriaBuilder.lessThan(path, year + 1);
                predicates.add(yearPredicate);
                predicates.add(nextYearPredicate);
            } else {
                Path<Date> path = root.get("grunnopplysninger").get("født").get("dato");
                final List<Predicate> datePredicates = createDatePredicates(criteriaBuilder, path, value);
                predicates.addAll(datePredicates);
            }
        }
                
        // Do not fetch those that have been "deleted".
        predicates.add(criteriaBuilder.or(criteriaBuilder.isNull(root.get("slettet")), 
                                          criteriaBuilder.isFalse(root.get("slettet"))));

        return predicates.toArray(new Predicate[0]);
    }
    
    private List<Predicate> createDatePredicates(final CriteriaBuilder criteriaBuilder,
                                                 final Path<Date> path, 
                                                 final String value) {
        final LocalDate date = GyldigeDatoformater.getLocalDate(value);
        final LocalDate nextDay = date != null ? date.plusDays(1) : null;
        final Predicate beginDatePredicate = criteriaBuilder.greaterThanOrEqualTo(path, GyldigeDatoformater.asLegacyDate(date));
        final Predicate endDatePredicate = criteriaBuilder.lessThan(path, GyldigeDatoformater.asLegacyDate(nextDay));
        
        return Arrays.asList(beginDatePredicate, endDatePredicate);
    }

    /**
     * Forces loading of the storage units which are lazy loaded.
     */
    private void forceLoadStorageUnits(List<Pasientjournal> medicalRecordList) {
        for (Pasientjournal medicalRecord: medicalRecordList) {
            if (medicalRecord.getLagringsenhet() != null) {
                final int tmp = medicalRecord.getLagringsenhet().size();
            }
        }
    }

    /**
     * Maps a result row from the database into a RecordTransferDTO object. It is dependant on order being correct.
     * @param resultRow A row from the database with the columns in correct order.
     * @return A new RecordTransferDTO object, initialized with the values from the result.
     */
    private RecordTransferDTO mapFromObjectsToRecordTransferDTO(final Object[] resultRow) {
        final String uuid = mapObjectToStringOrNull(resultRow[0]); 
        final String fanearkid = mapObjectToStringOrNull(resultRow[1]);
        final String daar = mapObjectToStringOrNull(resultRow[2]);
        final String faar = mapObjectToStringOrNull(resultRow[3]);
        final String pid = mapObjectToStringOrNull(resultRow[4]);
        final String pnavn = mapObjectToStringOrNull(resultRow[5]);
        final String journalnummer = mapObjectToStringOrNull(resultRow[6]);
        final String lopenummer = mapObjectToStringOrNull(resultRow[7]);
        final String oppdatertAv = mapObjectToStringOrNull(resultRow[8]);
        final Long opprettetDato = ((Date) resultRow[9]).getTime();
        final String avleveringsId = mapObjectToStringOrNull(resultRow[10]);
        final String lagringsenhet = mapObjectToStringOrNull(resultRow[11]);
        final boolean laast = (boolean) resultRow[12];

        return new RecordTransferDTO(uuid, lagringsenhet, pid, fanearkid, journalnummer, lopenummer, pnavn, faar, daar,
                                     oppdatertAv, avleveringsId, opprettetDato, laast);
    }

    /**
     * Helper object that converts an object to a string if it is not null, else returns null.
     * @param entry A string object.
     * @return The object as a string, or null if the object is null.
     */
    private String mapObjectToStringOrNull(final Object entry) {
        return entry != null ? entry.toString() : null;
    }
}
