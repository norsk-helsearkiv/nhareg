package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.CV;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Stateless
public class DiagnosisCodeDAO extends EntityDAO<Diagnosekode> {

    private static final String DISPLAY_NAME_LIKE_QUERY_PARAMETER = "displayNameLike";
    private static final String DIAGNOSE_DATE_QUERY_PARAMETER = "diagnoseDate";
    private static final String CODE_QUERY_PARAMETER = "code";

    public DiagnosisCodeDAO() {
        super(Diagnosekode.class, "code");
    }

    @Override
    protected Predicate[] extractPredicates(final Map<String, String> queryParameters,
                                            final CriteriaBuilder criteriaBuilder,
                                            final Root<Diagnosekode> root) {
        final List<Predicate> predicates = new ArrayList<Predicate>();

        final String codeQueryParameter = queryParameters.get(CODE_QUERY_PARAMETER);
        if (codeQueryParameter != null && !codeQueryParameter.isEmpty()) {
            final Predicate predicate = criteriaBuilder.equal(root.get("code"), codeQueryParameter);
            predicates.add(predicate);
        }

        final String dateQueryParameter = queryParameters.get(DIAGNOSE_DATE_QUERY_PARAMETER);
        if (dateQueryParameter != null && !dateQueryParameter.isEmpty()) {
            //datostreng kan bestå av kun år eller full dato.
            Date date = null;
            DatoEllerAar dateOrYear = DateOrYearConverter.toDateOrYear(dateQueryParameter);
            if (dateOrYear != null) {
                if (dateOrYear.getAar() != null) {
                    date = GyldigeDatoformater.getDateFromYear(dateOrYear.getAar());
                } else {
                    date = dateOrYear.getDato().getTime();
                }
            }

            if (date != null) {
                final String queryString =
                    "SELECT kodeverkversjon "
                        + "FROM Diagnosekodeverk "
                        + "WHERE gyldig_til_dato >= ?1 "
                        + "AND gyldig_fra_dato <= ?1";
                final List kodeverksversjoner = getEntityManager().createNativeQuery(queryString)
                                                                  .setParameter(1, new java.sql.Date(date.getTime()))
                                                                  .getResultList();
                kodeverksversjoner.size();
                //kan være flere kodeverk som overlapper her...
                EntityType<CV> type = getEntityManager().getMetamodel().entity(CV.class);
                final SingularAttribute<CV, String> attribute =
                    type.getDeclaredSingularAttribute("codeSystemVersion" , String.class);
                final Expression<String> expression = root.get(attribute);
                final Predicate predicate = expression.in(kodeverksversjoner);
                predicates.add(predicate);
            }
        }

        if (queryParameters.containsKey(DISPLAY_NAME_LIKE_QUERY_PARAMETER)) {
            createDisplayNamePredicate(queryParameters, criteriaBuilder, root, predicates);
        }

        return predicates.toArray(new Predicate[] {});
    }

    private void createDisplayNamePredicate(final Map<String, String> queryParameters,
                                            final CriteriaBuilder criteriaBuilder,
                                            final Root<Diagnosekode> root, 
                                            final List<Predicate> predicates) {
        final EntityType<CV> type = getEntityManager().getMetamodel().entity(CV.class);
        final String displayNameLike = queryParameters.get(DISPLAY_NAME_LIKE_QUERY_PARAMETER);
        final SingularAttribute<CV, String> attribute = type.getDeclaredSingularAttribute("displayName", String.class);
        final Expression<String> lower = criteriaBuilder.lower(root.get(attribute));
        final String pattern = "%" + displayNameLike.toLowerCase() + "%";
        final Predicate predicate = criteriaBuilder.like(lower, pattern);

        predicates.add(predicate);
    }
}