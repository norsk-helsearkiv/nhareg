package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import no.arkivverket.helsearkiv.nhareg.auth.Roller;
import no.arkivverket.helsearkiv.nhareg.auth.UserService;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.AvleveringDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * JAX-RS endepunkt for hÃƒÂ¥ndtering av {@link Avtale}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/avtaler")
@Stateless
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class AvtaleTjeneste extends EntitetsTjeneste<Avtale, String> {
    @EJB
    private UserService userService;
    @EJB
    private AvleveringTjeneste avleveringTjeneste;

    public AvtaleTjeneste() {
        super(Avtale.class, String.class, "avtaleidentifikator");
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Avtale> getAll(@Context UriInfo uriInfo) {
        List<Avtale> avtaler = getAll(uriInfo.getQueryParameters());
        return avtaler;
    }

    @GET
    @Path("/default")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDefaultAvtale(){
        Avlevering a = avleveringTjeneste.getDefaultAvlevering();
        if (a==null) {
            return null;
        }
        return a.getAvtale().getAvtaleidentifikator();
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

        Avlevering defaultAvlevering = avleveringTjeneste.getDefaultAvlevering();

        List<AvleveringDTO> dtoListe = new ArrayList<AvleveringDTO>();
        for(Avlevering a : avleveringer) {
            AvleveringDTO dto = new AvleveringDTO(a);
            if (defaultAvlevering!=null) {
                if (a.getAvleveringsidentifikator().equals(defaultAvlevering.getAvleveringsidentifikator())) {
                    dto.setDefaultAvlevering(true);
                }
            }
            dtoListe.add(dto);
        }
        
        return Response.ok(dtoListe).build();
    }
    @GET
    @Path("/virksomhet")
    public Virksomhet getVirksomhet(){
        List<Virksomhet> virksomheter = getEntityManager()
                .createQuery("SELECT v FROM Virksomhet v")
                .getResultList();
        //Setter virksomhet
        return virksomheter.get(0);
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

        Avtale other = getEntityManager().find(Avtale.class, avtale.getAvtaleidentifikator());
        if (other!=null){
            throw new EntityExistsException("Avtale med samme Id eksisterer");
        }
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
