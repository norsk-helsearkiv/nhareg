package no.arkivverket.helsearkiv.nhareg.medicalrecord;


import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Stateless
public class MedicalRecordDAO extends EntityDAO<Pasientjournal> {

    private final static String GET_RECORD_TRANSFER_RESULTS =
        "SELECT uuid, fanearkid, daar, faar, pid, pnavn,"
            + "journalnummer, lopenummer, ps.oppdatertAv, "
            + "opprettetDato, avleveringsidentifikator, "
            + "lagringsenhetformat, laast "
            + "FROM pasientjournal ps "
            + "JOIN avlevering_pasientjournal aps "
            + "ON ps.uuid = aps.pasientjournal_uuid "
            + "JOIN avlevering a "
            + "ON a.avleveringsidentifikator = aps.Avlevering_avleveringsidentifikator";

    private final static String GET_TRANSFER_ID_FROM_RECORD =
        "SELECT Avlevering_avleveringsidentifikator "
            + "FROM Avlevering_Pasientjournal "
            + "WHERE pasientjournal_uuid=?";

    private static final HashMap<String, String> PREDICATE_NATIVE_MAP = new HashMap<String, String>() {{
        put("fanearkid", "(fanearkid LIKE :fanearkid)");
        put("lagringsenhet", "(lagringsenhetformat LIKE :lagringsenhet)");
        put("fodselsnummer", "(pid LIKE :fodselsnummer)");
        put("navn", "(pnavn LIKE :navn)");
        put("oppdatertAv", "(ps.oppdatertAv LIKE :oppdatertAv)");
        put("sistOppdatert", "(ps.sistOppdatert BETWEEN :sistOppdatert AND :sistOppdatertEnd)");
        put("fodt", "(fdato BETWEEN :fodt AND :fodtEnd)");
        put("fodtYear", "(faar BETWEEN :fodt AND :fodtEnd)");
    }};
    
    public MedicalRecordDAO() {
        super(Pasientjournal.class, "uuid");
    }

    @Override
    public List<Pasientjournal> fetchAllPaged(final Map<String, String> queryParameters, final int page, final int size) {
        List<Pasientjournal> pagedResultList = super.fetchAllPaged(queryParameters, page, size);
        forceLoadStorageUnits(pagedResultList);
        return pagedResultList;
    }

    public List<RecordTransferDTO> fetchAllRecordTransfers(final Map<String, String> queryParameters,
                                                           final int page,
                                                           final int size) {
        final Query query = createQueryWithPredicates(queryParameters, GET_RECORD_TRANSFER_RESULTS);
    
        if (page > 0 && size >= 0) {
            // Remove 1 from the page as results are 0 indexed, but given page is not.
            query.setFirstResult(page - 1);
            query.setMaxResults(size);
        }

        List<Object[]> queryResults = query.getResultList();
        List<RecordTransferDTO> recordTransferDTOList = new ArrayList<>();

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
        Query query = this.getEntityManager().createNativeQuery(GET_TRANSFER_ID_FROM_RECORD);
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
            if (value != null && !value.isEmpty()) {
                final Path path = predicateMap.get(key);
                value = "%" + value.replace(' ', '%') + "%";
                predicates.add(criteriaBuilder.like(path, value));
            }
        }

        String key = "sistOppdatert";
        String value = queryParameters.get(key);
        if (value != null && !value.isEmpty()) {
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
        if (value != null && !value.isEmpty()) {
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

        // Do not fetch those that have been marked as deleted.
        predicates.add(criteriaBuilder.or(criteriaBuilder.isNull(root.get("slettet")),
                                          criteriaBuilder.isFalse(root.get("slettet"))));

        return predicates.toArray(new Predicate[0]);
    }

    
    /**
     * Creates a native query to the database based on the given query string. Converts the given query parameters to
     * SQL WHERE statements.
     * @param queryParameters Parameters to convert
     * @param queryString Base query string to attach predicates to.
     * @return The given query string with predicates as WHERE statements.
     */
    private Query createQueryWithPredicates(final Map<String, String> queryParameters, final String queryString) {
        final StringBuilder predicateStringBuilder = new StringBuilder(queryString);
        final Map<String, String> parameters = new HashMap<>();

        queryParameters.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                if (parameters.size() > 0) {
                    predicateStringBuilder.append(" AND ");
                }
                
                if ("sistOppdatert".equals(key) || "fodt".equals(key)) {
                    final String datePredicateNativeQuery = createDatePredicateNativeQuery(key, value, parameters);
                    predicateStringBuilder.append(datePredicateNativeQuery);
                } else {
                    parameters.put(key, "%" + queryParameters.get(key) + "%");
                    predicateStringBuilder.append(PREDICATE_NATIVE_MAP.get(key));
                }
            }
        });

        // Add the WHERE statement if any predicates were set.
        if (parameters.size() > 0) {
            predicateStringBuilder.insert(queryString.length(), " WHERE ");
        }

        // Replace parameters that were set
        final Query query = getEntityManager().createNativeQuery(predicateStringBuilder.toString());
        parameters.forEach((key, value) -> {
            // Dates needs to be set as DATE temporal type.
            if ("sistOppdatert".equals(key) || "sistOppdatertEnd".equals(key) ||
                "fodt".equals(key) || "fodtEnd".equals(key)) {
                if (value == null || value.isEmpty()) {
                    query.setParameter(key, "");
                } else {
                    final Date date = GyldigeDatoformater.getDate(value);
                    query.setParameter(key, date, TemporalType.DATE);
                }
            } else {
                query.setParameter(key, value);
            }
        });

        return query;
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
     * Creates a date predicate to be used in a WHERE ... BETWEEN statement. Adds start and end date to parameters.
     * @param key Key used to index PREDICATE_NATIVE_MAP. Is also used as key in parameters, key + End is used for 
     *            end date
     * @param value Date as a string of a valid date format ({@link GyldigeDatoformater}).
     * @param parameters Map that will be updated with the keys and values of the date and end date as strings or null
     *                   if the date could not be converted.
     * @return The {@link #PREDICATE_NATIVE_MAP} value for the given key.
     */
    private String createDatePredicateNativeQuery(final String key, 
                                                  final String value, 
                                                  final Map<String, String> parameters) {
        final LocalDate date = GyldigeDatoformater.getLocalDate(value);
        
        if (date == null) {
            parameters.put(key, null);
            parameters.put(key + "End", null);
            
            if ("fodt".equals(key) && value.length() == 4) {
                return PREDICATE_NATIVE_MAP.get("fodtYear");
            }
            return PREDICATE_NATIVE_MAP.get(key);
        }
        
        // Handle years.
        if (value.length() == 4) {
            final int nextYear = date.plusYears(1).getYear();
            final int year = date.getYear();
            
            parameters.put(key, String.valueOf(year));
            parameters.put(key + "End", String.valueOf(nextYear));

            // Special case where fodt is given by year, replace to search faar column instead.
            if ("fodt".equals(key)) {
                return PREDICATE_NATIVE_MAP.get("fodtYear");
            } else {
                return PREDICATE_NATIVE_MAP.get(key);
            }
        } else {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            final String day = date.format(formatter);
            final String nextDay = date.plusDays(1).format(formatter);

            parameters.put(key + "End", nextDay);
            parameters.put(key, day);
            return PREDICATE_NATIVE_MAP.get(key);
        }
    }
    
    /**
     * Maps a result row from the database into a RecordTransferDTO object. It is dependant on order being correct.
     * @param resultRow A row from the database with the columns in correct order.
     * @return A new RecordTransferDTO object, initialized with the values from the result.
     */
    private RecordTransferDTO mapFromObjectsToRecordTransferDTO(final Object[] resultRow) {
        final String uuid = Objects.toString(resultRow[0], null);
        final String fanearkid = Objects.toString(resultRow[1], null);
        final String daar = Objects.toString(resultRow[2], null);
        final String faar = Objects.toString(resultRow[3], null);
        final String pid = Objects.toString(resultRow[4], null);
        final String pnavn = Objects.toString(resultRow[5], null);
        final String journalnummer = Objects.toString(resultRow[6], null);
        final String lopenummer = Objects.toString(resultRow[7], null);
        final String oppdatertAv = Objects.toString(resultRow[8], null);
        final Long opprettetDato = ((Date) resultRow[9]).getTime();
        final String avleveringsId = Objects.toString(resultRow[10], null);
        final String lagringsenhet = Objects.toString(resultRow[11], null);
        final boolean laast = (boolean) resultRow[12];

        return new RecordTransferDTO(uuid, lagringsenhet, pid, fanearkid, journalnummer, lopenummer, pnavn, faar, daar,
                                     oppdatertAv, avleveringsId, opprettetDato, laast);
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

}
