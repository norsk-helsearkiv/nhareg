package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Diagnosekode}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/diagnosekoder")
/**
 * <p>
 * Dette er en stateless service, vi deklarer den som EJB for å få
 * transaksjonsstøtte.
 * </p>
 */
@Stateless
public class DiagnosekodeTjeneste extends EntitetsTjeneste<Diagnosekode, String> {

    public DiagnosekodeTjeneste() {
        super(Diagnosekode.class, String.class, "code");
    }

    public List<Diagnosekode> hentDiagnosekoderMedCode(String code) {
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
        queryParameters.add("code", code);
        List<Diagnosekode> diagnosekoder = getAll(queryParameters);
        return diagnosekoder;
    }

    @Override
    protected Predicate[] extractPredicates(MultivaluedMap<String, String> queryParameters,
            CriteriaBuilder criteriaBuilder,
            Root<Diagnosekode> root) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (queryParameters.containsKey("code")) {
            String code = queryParameters.getFirst("code");
            predicates.add(criteriaBuilder.equal(root.get("code"), code));
        }
        return predicates.toArray(new Predicate[]{});
    }

}
