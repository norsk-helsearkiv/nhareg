package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.List;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Avlevering}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/avleveringer")
@Stateless
public class AvleveringTjeneste extends EntitetsTjeneste<Avlevering, String> {

    public AvleveringTjeneste() {
        super(Avlevering.class, String.class, "avleveringsidentifikator");
    }

    /**
     * Henter pasientjournaler for en avlevering.
     *
     * @param avleveringsidentifikator
     * @return
     */
    @GET
    @Path("/{id}/pasientjournaler")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pasientjournal> getPasientjournaler(@PathParam("id") String avleveringsidentifikator) {
        List<Pasientjournal> pasientjournaler = super.getSingleInstance(avleveringsidentifikator).getPasientjournal();
        return pasientjournaler;
    }

}
