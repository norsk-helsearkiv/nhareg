package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.common.ValidDateFormats;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.RecordTransferDTO;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Stateless
public class MedicalRecordDAO extends EntityDAO<MedicalRecord> {

    private final static String RECORD_TRANSFER = 
        "FROM pasientjournal ps "
        + "JOIN avlevering_pasientjournal aps ON ps.uuid = aps.pasientjournal_uuid "
        + "JOIN avlevering a ON a.avleveringsidentifikator = aps.Avlevering_avleveringsidentifikator "
        + "WHERE (ps.slettet IS NULL) ";
    
    private final static String FETCH_RECORD_TRANSFER_RESULTS =
        "SELECT uuid, fanearkid, daar, faar, pid, pnavn,"
            + "journalnummer, lopenummer, ps.oppdatertAv, "
            + "opprettetDato, avleveringsidentifikator, "
            + "lagringsenhetformat, laast "
            + RECORD_TRANSFER;

    private final static String FETCH_RECORD_TRANSFER_COUNT = "SELECT COUNT(*) " + RECORD_TRANSFER;
    
    private static final HashMap<String, String> PREDICATE_NATIVE_MAP = new HashMap<String, String>() {{
        put("fanearkid", "(fanearkid LIKE :fanearkid)");
        put("lagringsenhet", "(lagringsenhetformat LIKE :lagringsenhet)");
        put("fodselsnummer", "(pid LIKE :fodselsnummer)");
        put("oppdatertAv", "(ps.oppdatertAv LIKE :oppdatertAv)");
        put("sistOppdatert", "(ps.sistOppdatert BETWEEN :sistOppdatert AND :sistOppdatertEnd)");
        put("fodt", "(fdato BETWEEN :fodt AND :fodtEnd)");
        put("fodtYear", "(faar BETWEEN :fodt AND :fodtEnd)");
        put("transferId", "(aps.Avlevering_avleveringsidentifikator = :transferId)");
    }};
    
    private static final List<String> DATE_PREDICATES = Arrays.asList("sistOppdatert", "sistOppdatertEnd", "fodt",
                                                                      "fodtEnd");

    private static final HashMap<String, String> ORDER_PREDICATES = new HashMap<String, String>() {{
        put("lagringsenhet", "lagringsenhetformat");
        put("fodselsnummer", "pid");
        put("jnr", "journalnummer");
        put("navn", "pnavn");
        put("lnr", "lopenummer");
    }};
    
    public MedicalRecordDAO() {
        super(MedicalRecord.class, "uuid");
    }

    @Override
    public MedicalRecord fetchById(final String id) {
        final MedicalRecord medicalRecord = super.fetchById(id);
        medicalRecord.getStorageUnit().size();
        medicalRecord.getDiagnosis().size();
        
        return medicalRecord;
    }

    @Override
    public MedicalRecord fetchSingleInstance(String id) throws NoResultException {
        final MedicalRecord medicalRecord = super.fetchSingleInstance(id);
        medicalRecord.getDiagnosis().size();
        medicalRecord.getStorageUnit().size();
        
        return medicalRecord;
    }

    public List<RecordTransferDTO> fetchAllRecordTransfers(final Map<String, String> queryParameters) {
        final Query query = createQueryWithPredicates(queryParameters, FETCH_RECORD_TRANSFER_RESULTS);
        final List<Object[]> queryResults;
        
        if (queryParameters.containsKey(PAGE) && queryParameters.containsKey(SIZE)) {
            final int page = Integer.parseInt(queryParameters.get(PAGE));
            final int size = Integer.parseInt(queryParameters.get(SIZE));
            final int index = (page - 1) * size;
            
            if (page > 0 && size >= 0) {
                // Remove 1 from the page as results are 0 indexed, but given page is not.
                query.setFirstResult(index);
                query.setMaxResults(size);
            }
        }

        try {
            queryResults = query.getResultList();
        } catch (IllegalArgumentException iae) {
            return new ArrayList<>();
        }

        // Map each row into a RecordTransferDTO object and add it to a list.
        final List<RecordTransferDTO> recordTransferDTOList = new ArrayList<>();
        queryResults.forEach(objects -> recordTransferDTOList.add(mapFromObjectsToRecordTransferDTO(objects)));
        
        return recordTransferDTOList;
    }

    public BigInteger fetchAllRecordTransferCount(final Map<String, String> queryParameters) {
        final Query query = createQueryWithPredicates(queryParameters, FETCH_RECORD_TRANSFER_COUNT);

        return (BigInteger) query.getSingleResult();
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
        
        if (queryParameters.size() <= 0) {
            return getEntityManager().createNativeQuery(queryString);
        }

        buildPredicateStringAndParameters(queryParameters, predicateStringBuilder, parameters);
        
        // Replace parameters that were set
        final Query query = getEntityManager().createNativeQuery(predicateStringBuilder.toString());
        replaceQueryParameters(parameters, query);

        return query;
    }

    private void buildPredicateStringAndParameters(final Map<String, String> queryParameters,
                                                   final StringBuilder predicateStringBuilder,
                                                   final Map<String, String> parameters) {
        queryParameters.forEach((key, value) -> {
            if (!PREDICATE_NATIVE_MAP.containsKey(key) && !"navn".equals(key)) {
                return;
            }
            
            predicateStringBuilder.append(" AND ");

            if (DATE_PREDICATES.contains(key)) {
                final String datePredicateNativeQuery = createDatePredicateNativeQuery(key, value, parameters);
                predicateStringBuilder.append(datePredicateNativeQuery);
            } else if ("navn".equals(key)) {
                final String namePredicateNativeQuery = createNamePredicateNativeQuery(value, parameters);
                predicateStringBuilder.append(namePredicateNativeQuery);
            } else if ("transferId".equals(key)) {
                parameters.put(key, queryParameters.get(key));
                predicateStringBuilder.append(PREDICATE_NATIVE_MAP.get(key));
            } else {
                parameters.put(key, "%" + queryParameters.get(key) + "%");
                predicateStringBuilder.append(PREDICATE_NATIVE_MAP.get(key));
            }
        });

        if (queryParameters.containsKey("orderBy") && queryParameters.containsKey("sortDirection")) {
            final String orderBy = queryParameters.get("orderBy");
            final String orderPredicate = ORDER_PREDICATES.getOrDefault(orderBy, orderBy);
            predicateStringBuilder.append(" ORDER BY ")
                                  .append(orderPredicate)
                                  .append(" ")
                                  .append(queryParameters.get("sortDirection").toUpperCase());
        }
    }

    private void replaceQueryParameters(final Map<String, String> parameters, final Query query) {
        parameters.forEach((key, value) -> {
            try {
                if (value == null || value.isEmpty()) {
                    query.setParameter(key, "");
                } else {
                    // Dates needs to be set as DATE temporal type.
                    if (DATE_PREDICATES.contains(key)) {
                        final Date date = ValidDateFormats.getDate(value);
                        query.setParameter(key, date, TemporalType.DATE);
                    } else {
                        query.setParameter(key, value);
                    }
                }
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
            }
        });
    }

    /**
     * Creates a predicate for pnavn column. For each name separated by a space it will create a query that matches
     * name% OR %name OR % name %. It will also convert any * characters to SQL wildcard characters.
     * @param value Names that will be split by space character.
     * @param parameters Map of parameters that will be updated with the keys and values for each given name.
     * @return A string to be inserted in a query behind a WHERE statement.
     */
    private String createNamePredicateNativeQuery(final String value, final Map<String, String> parameters) {
        final String[] valueArray = value.split(" ");
        final StringBuilder namesPredicate = new StringBuilder("(");

        Arrays.stream(valueArray).forEach(name -> {
            if (namesPredicate.length() > 2) {
                namesPredicate.append(" AND ");
            }
            
            // Replace * as it does not work with setParameter
            final String nameParameter = name.replace("*", "");
            // Matches name% OR %name OR % Name %
            namesPredicate.append("( pnavn LIKE :name").append(nameParameter)
                          .append(" OR pnavn LIKE :name").append(nameParameter).append("End")
                          .append(" OR pnavn LIKE :name").append(nameParameter).append("Middle )");

            // Replace * with SQL wildcard
            final String nameValue = name.replace('*', '%');
            parameters.put("name" + nameParameter, "%" + nameValue);
            parameters.put("name" + nameParameter+ "End", nameValue + "%");
            parameters.put("name" + nameParameter + "Middle", "% " + nameValue + " %");
        });

        namesPredicate.append(")");

        return namesPredicate.toString();
    }

    /**
     * Creates a date predicate to be used in a WHERE ... BETWEEN statement. Adds start and end date to parameters.
     * @param key Key used to index PREDICATE_NATIVE_MAP. Is also used as key in parameters, key + End is used for 
     *            end date
     * @param value Date as a string of a valid date format ({@link ValidDateFormats}).
     * @param parameters Map that will be updated with the keys and values of the date and end date as strings or null
     *                   if the date could not be converted.
     * @return The {@link #PREDICATE_NATIVE_MAP} value for the given key.
     */
    private String createDatePredicateNativeQuery(final String key, final String value, final Map<String, String> parameters) {
        final LocalDate date = ValidDateFormats.getLocalDate(value);

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
        final long fanearkid = Long.parseLong(Objects.toString(resultRow[1], "0"));
        final String deathYear = Objects.toString(resultRow[2], null);
        final String bornYear = Objects.toString(resultRow[3], null);
        final String pid = Objects.toString(resultRow[4], null);
        final String name = Objects.toString(resultRow[5], null);
        final String recordNumber = Objects.toString(resultRow[6], null);
        final String serialNumber = Objects.toString(resultRow[7], null);
        final String updatedBy = Objects.toString(resultRow[8], null);
        final Date creationDate = resultRow[9] == null ? null : (Date) resultRow[9];
        final Long creationDateLong = creationDate == null ? null : creationDate.getTime();
        final String transferId = Objects.toString(resultRow[10], null);
        final String storageUnit = Objects.toString(resultRow[11], null);
        final boolean transferLocked = (boolean) resultRow[12];

        return new RecordTransferDTO(uuid, storageUnit, pid, fanearkid, recordNumber, serialNumber, name, bornYear,
                                     deathYear, updatedBy, transferId, creationDateLong, transferLocked);
    }

}