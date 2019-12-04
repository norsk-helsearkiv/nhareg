package no.arkivverket.helsearkiv.nhareg.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MultivaluedMap;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;

/**
 * Created by haraldk on 16/03/2017.
 */
public class PasientjournalExtractPredicates {

    public Predicate[] extractPredicates(MultivaluedMap<String, String> queryParameters, CriteriaBuilder criteriaBuilder, Root<Pasientjournal> root) {
        final List<Predicate> predicates = new ArrayList<Predicate>();

        String key = "fanearkid";
        String value = queryParameters.getFirst(key);

        if (!StringUtils.isEmpty(value)) {
            value = "%" + value + "%";
            predicates.add(criteriaBuilder.like(root.<String>get(key), value));
        }
        
        key = "lagringsenhet";
        value = queryParameters.getFirst(key);
        if (!StringUtils.isEmpty(value)) {
            value = "%" + value +"%";
            Join<Pasientjournal, Lagringsenhet> lagringsenhet = root.join(key);
            predicates.add(criteriaBuilder.like(lagringsenhet.<String>get("identifikator"), value));
        }

        key ="fodselsnummer";
        value = queryParameters.getFirst(key);
        if (!StringUtils.isEmpty(value)){
            value = "%" + value + "%";
            predicates.add(criteriaBuilder.like(root.get("grunnopplysninger").get("identifikator").<String>get("pid"), value));
        }

        key="navn";
        value = queryParameters.getFirst(key);
        if (!StringUtils.isEmpty(value)){
            value = "%" + value + "%";
            predicates.add(criteriaBuilder.like(root.get("grunnopplysninger").<String>get("pnavn"), value));
        }



        key = "oppdatertAv";
        value = queryParameters.getFirst(key);
        if (!StringUtils.isEmpty(value)){
            value = "%" + value + "%";
            predicates.add(criteriaBuilder.like(root.get("oppdateringsinfo").<String>get("oppdatertAv"), value));
        }

        key = "sistOppdatert";
        value = queryParameters.getFirst(key);
        if (!StringUtils.isEmpty(value)){
            Date date = GyldigeDatoformater.getDate(value);
            addEqualsDateDepth2(predicates, criteriaBuilder, root, date, "oppdateringsinfo", "sistOppdatert");
        }
        key = "fodt";
        value = queryParameters.getFirst(key);
        if (!StringUtils.isEmpty(value)){
            if (value.length()==4){
                //år
                Integer aar = Integer.valueOf(value);
                Predicate aarPredicate = criteriaBuilder.equal(root.get("grunnopplysninger").get("født").<Integer>get("aar"), aar);

                //or født dato
                Date start = toStartOfYear(aar);
                Date end = toEndOfYear(aar);
                Predicate[] datoPredicate = getEqualsDateDepth3(criteriaBuilder, root, start, end, "grunnopplysninger", "født", "dato");
                predicates.add(criteriaBuilder.or(aarPredicate, criteriaBuilder.and(datoPredicate[0], datoPredicate[1])));
            }
            else{
                Date date = GyldigeDatoformater.getDate(value);
                predicates.addAll(Arrays.asList(getEqualsDateDepth3(criteriaBuilder, root, date, null, "grunnopplysninger", "født", "dato")));
            }
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void addEqualsDateDepth2(List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<Pasientjournal> root, Date date, String firstAtt, String secondAtt){
        Date dateEnd = GyldigeDatoformater.getDateRollDay(date, 1);
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(firstAtt).<Date>get(secondAtt), date));
        predicates.add(criteriaBuilder.lessThan(root.get(firstAtt).<Date>get(secondAtt), dateEnd));
    }
    private Predicate[] getEqualsDateDepth3(CriteriaBuilder criteriaBuilder, Root<Pasientjournal> root, Date date, Date dateEnd, String firstAtt, String secondAtt, String thirdAtt){
        if (dateEnd==null){
            dateEnd = GyldigeDatoformater.getDateRollDay(date, 1);
        }

        return new Predicate[]{
                criteriaBuilder.greaterThanOrEqualTo(root.get(firstAtt).get(secondAtt).<Date>get(thirdAtt), date),
                criteriaBuilder.lessThan(root.get(firstAtt).get(secondAtt).<Date>get(thirdAtt), dateEnd)
        };
    }

    public static Date toStartOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, -1);
        return calendar.getTime();
    }

    public static Date toEndOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year+1);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,-1);
        return calendar.getTime();
    }
}
