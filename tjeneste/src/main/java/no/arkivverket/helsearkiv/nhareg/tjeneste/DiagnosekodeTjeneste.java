package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import no.arkivverket.helsearkiv.nhareg.auth.Roller;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.CV;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.DagEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;
import no.arkivverket.helsearkiv.nhareg.transformer.Konverterer;
import org.apache.commons.lang3.StringUtils;

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
    public static final String DIAGNOSE_DATE_QUERY_PARAMETER = "diagnoseDate";
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

        if (queryParameters.containsKey(DIAGNOSE_DATE_QUERY_PARAMETER) &&
                !StringUtils.isEmpty(queryParameters.getFirst(DIAGNOSE_DATE_QUERY_PARAMETER))){

            String diagnoseDateString = queryParameters.getFirst(DIAGNOSE_DATE_QUERY_PARAMETER);
            //datostreng kan bestå av kun år eller full dato.
            Date d = null;
            try {

                DatoEllerAar dea = Konverterer.tilDatoEllerAar(diagnoseDateString);
                if (dea.getAar()!=null){
                    d = GyldigeDatoformater.getDateFromYear(dea.getAar());
                }else{
                    d = dea.getDato().getTime();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (d!=null){
                List<String> kodeverksversjoner = getEntityManager().createNativeQuery("select kodeverkversjon from Diagnosekodeverk where gyldig_til_dato >=?1 and gyldig_fra_dato <= ?1")
                        .setParameter(1, new java.sql.Date(d.getTime()))
                        .getResultList();
                kodeverksversjoner.size();
                //kan være flere kodeverk som overlapper her...
                EntityType<CV> type = getEntityManager().getMetamodel().entity(CV.class);
                Expression<String> expression = root.get(type.getDeclaredSingularAttribute("codeSystemVersion", String.class));
                Predicate p = expression.in(kodeverksversjoner);
                predicates.add(p);

            }
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
