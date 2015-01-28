package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;

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
     * @param uriInfo
     * @param avleveringsidentifikator
     * @return
     */
    @GET
    @Path("/{id}/pasientjournaler")
    @Produces(MediaType.APPLICATION_JSON)
    public ListeObjekt getPasientjournaler(@Context UriInfo uriInfo, @PathParam("id") String avleveringsidentifikator) {
        //Hent total count
        String totalJpql = "SELECT a FROM Avlevering a WHERE a.avleveringsidentifikator LIKE :id";
        Query totalQuery = super.getEntityManager().createQuery(totalJpql);
        totalQuery.setParameter("id", avleveringsidentifikator);
        Avlevering avlevering = (Avlevering) totalQuery.getSingleResult();
        
        //Hent url parameter
        int total = avlevering.getPasientjournal().size();
        int forste = 0;
        int side = 1;
        int antall = total;
        
        if(uriInfo != null) {
            MultivaluedMap<String, String> queryParameters = uriInfo.getPathParameters();
            if (queryParameters.containsKey("side") && queryParameters.containsKey("antall")) {
                int paramSide = Integer.parseInt(queryParameters.getFirst("side")) - 1;
                int paramAntall = Integer.parseInt(queryParameters.getFirst("antall"));

                if(paramSide > 0 && paramAntall > 0) {
                    side = paramSide;
                    antall = paramAntall;

                    forste = (side * antall) -1;
                }
            }
        }
        
        List<Pasientjournal> pasientjournaler = new ArrayList<Pasientjournal>();
        for(int i = forste; i < antall; i++) {
            pasientjournaler.add(avlevering.getPasientjournal().get(i));
        }
        
        //Returner objekt
        return new ListeObjekt(pasientjournaler, total, side, antall);
    }
    
    
    @GET
    @Path("/{id}/leveranse")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getLeveranse(@PathParam("id") String avleveringsidentifikator) throws FileNotFoundException {
        //Tmp fil
        File file = new File("leveranse");
        Formatter out = new Formatter(file);
        out.format("data");
        out.close();
        
        //Generer XML
        
        //Legg til vedlegg
        
        //Returner fil
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=" + avleveringsidentifikator + ".zip");
        return response.build();
    }

    @DELETE
    @Path("/{id}")
    @Override
    public Response delete(@PathParam("id") String id) {
        Avlevering avlevering = super.getEntityManager().find(Avlevering.class, id);
        if (avlevering == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        //Slett om det ikke er barn
        if(avlevering.getPasientjournal().isEmpty()) {
            getEntityManager().remove(avlevering);
            return Response.ok().build();
        } 
        return Response.status(409).build();
    }
}
