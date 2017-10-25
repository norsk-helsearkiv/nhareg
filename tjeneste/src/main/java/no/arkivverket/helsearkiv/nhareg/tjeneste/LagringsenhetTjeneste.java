package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import no.arkivverket.helsearkiv.nhareg.auth.Roller;
import no.arkivverket.helsearkiv.nhareg.auth.UserService;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.FlyttPasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.Validator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Lagringsenhet}er. Arver metodene
 * fra {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
    @Path("/lagringsenheter")
/**
 * <p>
 * Dette er en stateless service, vi deklarer den som EJB for å få
 * transaksjonsstøtte.
 * </p>
 */
@Stateless
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class LagringsenhetTjeneste extends EntitetsTjeneste<Lagringsenhet, String> {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final javax.validation.Validator validator = factory.getValidator();
    @Resource
    private SessionContext sessionContext;
    @EJB
    private UserService userTjeneste;
    @EJB
    private AvleveringTjeneste avleveringTjeneste;

    @EJB
    private PasientjournalTjeneste pasientjournalTjeneste;

    public LagringsenhetTjeneste() {
        super(Lagringsenhet.class, String.class, "uuid");

    }

    @PUT
    @RolesAllowed(value = {Roller.ROLE_ADMIN})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response oppdaterPasientjournal(Lagringsenhet lagringsenhet) throws ParseException {
        String pasientjournalUuid = getPasientjournalUuid(lagringsenhet.getUuid());
        String avleveringsId = avleveringTjeneste.getAvleveringsidentifikator(pasientjournalUuid);

        //sjekk om lagringsenhet finnes i en annen avlevering
        List<Valideringsfeil> valideringsfeil = avleveringTjeneste.validerLagringsenheter(avleveringsId, Arrays.asList(lagringsenhet));
        if (!valideringsfeil.isEmpty()) {
            Valideringsfeil feil = new Valideringsfeil("identifikator", "Lagringsenhetens identifikator finnes i en annen avlevering, benytt en annen identifikator");

            return Response.status(Response.Status.BAD_REQUEST).entity(Arrays.asList(feil)).build();
        }

        Integer count = getLagringsenhetCount(lagringsenhet.getIdentifikator());
        if (count>0){
            Valideringsfeil feil = new Valideringsfeil("identifikator", "Lagringsenhetens identifikator er ikke unik, benytt en annen identifikator");
            return Response.status(Response.Status.BAD_REQUEST).entity(Arrays.asList(feil)).build();
        }

        Lagringsenhet updated = update(lagringsenhet);
        return Response.ok(updated).build();
    }

    public final Integer getLagringsenhetCount(String lagringsenhetIdentifikator){
        Query query = getEntityManager().createNativeQuery("select count(*) from Lagringsenhet where identifikator=?");
        query.setParameter(1, lagringsenhetIdentifikator);

        BigInteger result = (BigInteger) query.getSingleResult();
        return Integer.valueOf(result.intValue());
    }
    public final String getPasientjournalUuid(String lagringsenhetUuid){
        Query query = getEntityManager().createNativeQuery("select Pasientjournal_uuid from Pasientjournal_Lagringsenhet where lagringsenhet_uuid=?");
        query.setParameter(1, lagringsenhetUuid);
        query.setMaxResults(1);
        Object result = query.getSingleResult();
        return String.valueOf(result);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {"admin", "bruker"})
    @Path("/sistBrukte")
    public String getSistBrukteLagringsenhet(){
        final String username = sessionContext.getCallerPrincipal().getName();
        final String lagringsenhet = userTjeneste.getLagringsenhet(username);
        return lagringsenhet;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {"admin", "bruker"})
    @Path("/{id}/pasientjournaler")
    public List<PasientjournalSokeresultatDTO> getPasientjournaler(@PathParam("id") final String id){
        List<PasientjournalSokeresultatDTO> list = pasientjournalTjeneste.hentPasientjournalerForLagringsenhet(id);

        return list;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roller.ROLE_ADMIN})
    @Path("/flytt")
    public Response flyttPasientjournaler(final FlyttPasientjournalDTO lagringsenhet){
        Lagringsenhet enhet = hentLagringsenhetMedIdentifikator(lagringsenhet.getLagringsenhetIdentifikator());
        if (enhet==null){
            Valideringsfeil feil = new Valideringsfeil("identifikator", "Lagringsenheten finnes ikke");
            return Response.status(Response.Status.BAD_REQUEST).entity(Arrays.asList(feil)).build();
        }

        int updated = oppdaterPasientjournalLagringsenhet(lagringsenhet.getPasientjournalUuids(), enhet);

        return Response.ok(updated).build();
    }

    private int oppdaterPasientjournalLagringsenhet(List<String> uuids, Lagringsenhet enhet){
        for (String uuid: uuids){
            Pasientjournal p = getEntityManager().find(Pasientjournal.class, uuid);
            p.getLagringsenhet().clear();
            p.getLagringsenhet().add(enhet);
        }

        return 0;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {"admin", "bruker"})
    @Path("/sok")
    public List<Lagringsenhet> sokLagringsenhetMedIdentifikator(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        String identifikatorSok = queryParameters.getFirst("identifikatorSok");

        String select = "select object(o)"
                + "  from Lagringsenhet as o"
                + " where o.identifikator LIKE :identifikator"
                + "  order by o.uuid";
        final Query query = getEntityManager().createQuery(select);
        query.setParameter("identifikator", "%"+identifikatorSok+"%");
        List<Lagringsenhet> lagringsenheter = query.getResultList();

        return lagringsenheter;
    }

    @Override
    public Lagringsenhet create(Lagringsenhet entity) {
        //
        // Lagringsenhet.identifikator skal være unikt,
        // så vi må hindre at det opprettes flere med samme identifikator.
        //
                
        ArrayList<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();
        if (entity == null) {
            valideringsfeil.add(new Valideringsfeil("Lagringsenhet", "NotNull"));
            throw new ValideringsfeilException(valideringsfeil);
        } else if (hentLagringsenhetMedIdentifikator(entity.getIdentifikator()) != null) {
            valideringsfeil.add(new Valideringsfeil("Lagringsenhet.identifikator", "NotNull"));
            throw new ValideringsfeilException(valideringsfeil);
        }
        entity.setUuid(UUID.randomUUID().toString());
        //
        // Validering
        //
        new Validator<Lagringsenhet>(Lagringsenhet.class).validerMedException(entity);
        //
        // Oppretter
        //
        return super.create(entity);
    }

    /**
     * Henter lagringsenhet med identifikator.
     *
     * @param identifikator
     * @return Lagringsenhet.
     */
    public Lagringsenhet hentLagringsenhetMedIdentifikator(String identifikator) {
        Lagringsenhet lagringsenhet = null;
        String select = "select object(o)"
                + "  from Lagringsenhet as o"
                + " where o.identifikator = :identifikator"
                + "  order by o.uuid";
        final Query query = getEntityManager().createQuery(select);
        query.setParameter("identifikator", identifikator);
        List<Lagringsenhet> lagringsenheter = query.getResultList();
        //
        // Skal være en liste med max en forekomst.
        // Hvis det er flere tar vi den med lavest uuid.
        //
        if (!lagringsenheter.isEmpty()) {
            lagringsenhet = lagringsenheter.get(0);
        }
        return lagringsenhet;
    }
    
    /**
     * Henter Lagringsenheter for en avlevering.
     * @param avleveringsidentifikator
     * @return 
     */
    public List<Lagringsenhet> hentLagringsenheterForAvlevering(String avleveringsidentifikator) {
        String select = "SELECT distinct l"
                +"         FROM Avlevering a"
                + "  INNER JOIN a.pasientjournal p"
                + "  INNER JOIN p.lagringsenhet l"
                + "       WHERE a.avleveringsidentifikator = :avleveringsidentifikator"
                + "    ORDER BY l.identifikator";
        final Query query = getEntityManager().createQuery(select);
        query.setParameter("avleveringsidentifikator", avleveringsidentifikator);
        List<Lagringsenhet> lagringsenheter = query.getResultList();
        return lagringsenheter;
    }
}
