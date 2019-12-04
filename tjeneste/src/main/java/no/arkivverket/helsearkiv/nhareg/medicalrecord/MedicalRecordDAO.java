package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;


@Stateless
public class MedicalRecordDAO extends EntityDAO<Pasientjournal> {
    
    public MedicalRecordDAO() {
        super(Pasientjournal.class, "uuid");
    }

    @Override
    public List<Pasientjournal> getAll(final Map<String, String> queryParameters) {
        // TODO sokestring query results
        final List<Pasientjournal> resultList = super.getAll(queryParameters);
        fetchStorageUnits(resultList);
        return resultList;
    }

    @Override
    public List<Pasientjournal> getAllPaged(final Map<String, String> queryParameters, 
                                            final int page,
                                            final int number) {
        final List<Pasientjournal> pagedResultList = super.getAllPaged(queryParameters, page, number);
        fetchStorageUnits(pagedResultList);
        return pagedResultList;
    }

    /**
     * Get the delivery id (avleverings id) associated with the medical record (pasientjournal) id.
     * @param id Medical record id (pasientjournal id)
     * @return the delivery id as an int
     */
    public int getDeliveryIdFromMedicalRecord(final String id) {
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
            put("fanearkid", root.get("fanearkid"));
            put("lagringsenhet", root.join("lagringsenhet").get("identifikator"));
            put("fodselsnummer", root.get("grunnopplysninger").get("identifikator").get("pid"));
            put("navn", root.get("grunnopplysninger").get("pnavn"));
            put("oppdatertAv", root.get("oppdateringsinfo").get("oppdatertAv"));
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
                final List<Predicate> datePredicates = getDatePredicates(criteriaBuilder, path, value);
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
                final List<Predicate> datePredicates = getDatePredicates(criteriaBuilder, path, value);
                predicates.addAll(datePredicates);
            }
        }
        
        key = "avlevering";
        value = queryParameters.get(key);
        if (!value.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get(""), value));
        }
        
        // Do not fetch those that have been "deleted".
        predicates.add(criteriaBuilder.or(criteriaBuilder.isNull(root.get("slettet")), 
                                          criteriaBuilder.isFalse(root.get("slettet"))));

        return predicates.toArray(new Predicate[0]);
    }
    
    private List<Predicate> getDatePredicates(final CriteriaBuilder criteriaBuilder, final Path<Date> path, final String value) {
        final LocalDate date = GyldigeDatoformater.getLocalDate(value);
        final LocalDate nextDay = date != null ? date.plusDays(1) : null;
        final Predicate beginDatePredicate = criteriaBuilder.greaterThanOrEqualTo(path, GyldigeDatoformater.asLegacyDate(date));
        final Predicate endDatePredicate = criteriaBuilder.lessThan(path, GyldigeDatoformater.asLegacyDate(nextDay));
        
        return Arrays.asList(beginDatePredicate, endDatePredicate);
    }

    /**
     * Forces loading of the storage units which are lazy loaded.
     */
    private void fetchStorageUnits(List<Pasientjournal> medicalRecordList) {
        for (Pasientjournal medicalRecord: medicalRecordList) {
            if (medicalRecord.getLagringsenhet() != null) {
                final int tmp = medicalRecord.getLagringsenhet().size();
            }
        }
    }
}
