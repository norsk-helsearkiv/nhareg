package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Path;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
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
public class LagringsenhetTjeneste extends EntitetsTjeneste<Lagringsenhet, String> {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final javax.validation.Validator validator = factory.getValidator();

    public LagringsenhetTjeneste() {
        super(Lagringsenhet.class, String.class, "uuid");

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
