package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Avtale}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/avtaler")
@Stateless
public class AvtaleTjeneste extends EntitetsTjeneste<Avtale, String> {

    public AvtaleTjeneste() {
        super(Avtale.class, String.class, "avtaleidentifikator");
    }

    /**
     * Henter avleveringer for en avtale.
     *
     * @param avtaleidentifikator
     * @return
     */
    @GET
    @Path("/{id}/avleveringer")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Avlevering> getAvleveringer(@PathParam("id") String avtaleidentifikator) {
        String select = "select object(o)"
                + "  from Avlevering as o"
                + " where o.avtale.avtaleidentifikator = :avtaleidentifikator";
        final Query query = getEntityManager().createQuery(select);
        query.setParameter("avtaleidentifikator", avtaleidentifikator);
        List<Avlevering> avleveringer = query.getResultList();
        return avleveringer;
    }
}
