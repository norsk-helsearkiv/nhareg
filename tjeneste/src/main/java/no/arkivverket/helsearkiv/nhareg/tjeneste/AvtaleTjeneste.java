package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;

/**
 * <p>
 * JAX-RS endepunkt for hÃƒÂ¥ndtering av {@link Avtale}r. Arver metodene fra
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
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        Avtale avtale = super.getEntityManager().find(Avtale.class, id);
        if (avtale == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        //Hent antall barn
        String jpql = "SELECT count(a) FROM Avlevering a WHERE a.avtale = :avtale";
        Query q = super.getEntityManager().createQuery(jpql);
        q.setParameter("avtale", avtale);
        Long antall = (Long) q.getSingleResult();
        
        //Slett om det ikke er barn
        if(antall == 0) {
            getEntityManager().remove(avtale);
            return Response.ok().build();
        } 
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
}
