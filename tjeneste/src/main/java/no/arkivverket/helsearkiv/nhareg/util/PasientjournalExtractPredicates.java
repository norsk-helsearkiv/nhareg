package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Identifikator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;

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

        key = "fodt";//TODO
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

        key = "sistOppdatert";//TODO
        value = queryParameters.getFirst(key);
        if (!StringUtils.isEmpty(value)){
            value = "%" + value + "%";
            predicates.add(criteriaBuilder.like(root.get("grunnopplysninger").<String>get("pnavn"), value));
        }return predicates.toArray(new Predicate[predicates.size()]);
    }

}
