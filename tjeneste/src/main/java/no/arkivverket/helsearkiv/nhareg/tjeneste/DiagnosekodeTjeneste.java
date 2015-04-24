package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import no.arkivverket.helsearkiv.nhareg.auth.Roller;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.CV;
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
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class DiagnosekodeTjeneste extends EntitetsTjeneste<Diagnosekode, String> {

    public static final String DISPLAY_NAME_LIKE_QUERY_PARAMETER = "displayNameLike";
    public static final String CODE_QUERY_PARAMETER = "code";

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

        if (queryParameters.containsKey(CODE_QUERY_PARAMETER)) {
            String code = queryParameters.getFirst(CODE_QUERY_PARAMETER);
            Predicate p = criteriaBuilder.equal(root.get("code"), code);
            predicates.add(p);
        }
        if (queryParameters.containsKey(DISPLAY_NAME_LIKE_QUERY_PARAMETER)) {
            EntityType<CV> type = getEntityManager().getMetamodel().entity(CV.class);
            String displayNameLike = queryParameters.getFirst(DISPLAY_NAME_LIKE_QUERY_PARAMETER);
            Predicate p = criteriaBuilder.like(
                    criteriaBuilder.lower(
                            root.get(
                                    type.getDeclaredSingularAttribute("displayName", String.class)
                            )
                    ), "%" + displayNameLike.toLowerCase() + "%");
            predicates.add(p);
        }
        return predicates.toArray(new Predicate[]{});
    }
}
