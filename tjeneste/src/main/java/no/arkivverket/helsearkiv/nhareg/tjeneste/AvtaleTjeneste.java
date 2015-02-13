package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;

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
    public Response getAvleveringer(@PathParam("id") String avtaleidentifikator) {
        String select = "select object(o)"
                + "  from Avlevering as o"
                + " where o.avtale.avtaleidentifikator = :avtaleidentifikator";
        final Query query = getEntityManager().createQuery(select);
        query.setParameter("avtaleidentifikator", avtaleidentifikator);
        List<Avlevering> avleveringer = query.getResultList();
        
        return Response.ok(avleveringer).build();
    }
    
    @POST
    @Override
    public Avtale create(Avtale avtale) {
        //Henter virksomhet
        List<Virksomhet> virksomheter = getEntityManager()
                .createQuery("SELECT v FROM Virksomhet v")
                .getResultList();
        //Setter virksomhet
        avtale.setVirksomhet(virksomheter.get(0));
        //Oppretter avtale
        return super.create(avtale);
    }
    
    @DELETE
    @Path("/{id}")
    @Override
    public Avtale delete(@PathParam("id") String id) {
        Avtale avtale = getSingleInstance(id);
        
        //Hent antall barn
        String jpql = "SELECT count(a) FROM Avlevering a WHERE a.avtale = :avtale";
        Query q = super.getEntityManager().createQuery(jpql);
        q.setParameter("avtale", avtale);
        Long antall = (Long) q.getSingleResult();
        
        //Slett om det ikke er barn
        if(antall == 0) {
            getEntityManager().remove(avtale);
            return avtale;
        } 
        
        ArrayList<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();
        valideringsfeil.add(new Valideringsfeil("Avtale", "HasChildren"));
        throw new ValideringsfeilException(valideringsfeil);
    }
    
}
