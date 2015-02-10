package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
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
        String select = "select object(o)"
                + "  from Diagnosekode as o"
                + " where o.code = :code";
        final Query query = getEntityManager().createQuery(select);
        query.setParameter("code", code);
        List<Diagnosekode> diagnosekoder = query.getResultList();
        return diagnosekoder;
    }
}
