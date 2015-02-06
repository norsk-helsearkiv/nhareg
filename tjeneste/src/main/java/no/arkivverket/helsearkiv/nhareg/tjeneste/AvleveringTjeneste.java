package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.Validator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.util.DatoValiderer;
import no.arkivverket.helsearkiv.nhareg.util.Konverterer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    
    Log log = LogFactory.getLog(AvleveringTjeneste.class);

    public AvleveringTjeneste() {
        super(Avlevering.class, String.class, "avleveringsidentifikator");
    }

    /**
     * Henter pasientjournaler for en avlevering.
     *
     * @param avleveringsidentifikator 
     * @param uriInfo 
     * @return Liste over pasientjournaler
     */
    @GET
    @Path("/{id}/pasientjournaler")
    @Produces(MediaType.APPLICATION_JSON)
    public ListeObjekt getPasientjournaler(@PathParam("id") String avleveringsidentifikator, @Context UriInfo uriInfo) {
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
        
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        if (queryParameters.containsKey("side") && queryParameters.containsKey("antall")) {
            Integer qSide = Integer.parseInt(queryParameters.getFirst("side"));
            Integer qAntall = Integer.parseInt(queryParameters.getFirst("antall"));
            
            if(qSide > 0 && qAntall > 0) {
                side = qSide;
                antall = qAntall;
                forste = (side - 1) * antall;
            }
        }
        
        List<Pasientjournal> aktiveJournaler = new ArrayList<Pasientjournal>();
        List<Pasientjournal> pasientjournaler = avlevering.getPasientjournal();
        int totalAktive = 0;
        int antallIListe = 0;

        for(int i = 0; i < total; i++) {
            //Aktiv
            if(pasientjournaler.get(i).isSlettet() == null ||
                    !pasientjournaler.get(i).isSlettet()) {
                
                if(antallIListe <= antall) {
                    aktiveJournaler.add(pasientjournaler.get(i));
                    antallIListe++;
                }
                totalAktive++;
            }            
        }
        
        //Returner objekt
        return new ListeObjekt(aktiveJournaler, totalAktive, side, antall);
    }
        
    /**
     * Oppretter en ny pasientjournal under avleveringen
     * @param avleveringid
     * @param person
     * @return Pasientjournal
     * @throws java.text.ParseException av datoteksten til Date objekt for å sammenligne størrelse
     */
    @POST
    @Path("/{id}/pasientjournaler")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response nyPasientjournal(@PathParam("id") String avleveringid, PersondataDTO person) throws ParseException {
        // VALIDERING
        ArrayList<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();
        if(person == null) {
            valideringsfeil.add(new Valideringsfeil(PersondataDTO.class + "", "NotNull"));
            return Response.status(Response.Status.BAD_REQUEST).entity(valideringsfeil).build();
        }
        valideringsfeil = new Validator<PersondataDTO>(PersondataDTO.class, person).valider();
        valideringsfeil.addAll(DatoValiderer.valider(person));

        if(!valideringsfeil.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(valideringsfeil).build();
        }
        
        //KONVERTERING
        Pasientjournal pasientjournal = Konverterer.fraDTO(person);
        
        //SETTER VERDIER
        pasientjournal.setUuid(UUID.randomUUID().toString());
        
        //Setter visningsnavnet til det som er lagret i databasen for kjønn med koden
        if(person.getKjonn() != null) {
            //Får feilmelding ved bruk av WHERE, unexpected Hex 
            Query q = getEntityManager().createQuery("SELECT k FROM Kjønn k");
            List<Kjønn> liste = q.getResultList();
            for(Kjønn k : liste) {
                if(k.getCode().equals(person.getKjonn())) {
                    pasientjournal.getGrunnopplysninger().setKjønn(k);
                }
            }
        }

        //LAGRER
        getEntityManager().persist(pasientjournal);
        Avlevering avlevering = getEntityManager().find(Avlevering.class, avleveringid);
        avlevering.getPasientjournal().add(pasientjournal);
        return Response.ok().entity(pasientjournal).build();
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
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response oppdaterAvtale(Avlevering avlevering) {
        return super.oppdaterAvtale(avlevering, avlevering.getAvleveringsidentifikator());
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
