package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.util.DatoValiderer;
import no.arkivverket.helsearkiv.nhareg.util.Konverterer;

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
    
    @EJB
    private PasientjournalTjeneste pasientjournalTjeneste;
    @EJB
    private KjønnTjeneste kjønnTjeneste;
    
    public AvleveringTjeneste() {
        super(Avlevering.class, String.class, "avleveringsidentifikator");
    }

    /**
     * Henter pasientjournaler for en avlevering. Skal kun returnere pasientjournaler
     * til en avlevering som ikke er slettet.
     * Har også støtte for paging med query parameter 'side' og 'antall'
     *
     * @param avleveringsidentifikator 
     * @param uriInfo 
     * @return Liste over pasientjournaler
     */
    @GET
    @Path("/{id}/pasientjournaler")
    @Produces(MediaType.APPLICATION_JSON)
    public ListeObjekt getPasientjournaler(@PathParam("id") String avleveringsidentifikator, @Context UriInfo uriInfo) {
        Avlevering avlevering = (Avlevering) getSingleInstance(avleveringsidentifikator).getEntity();
        return pasientjournalTjeneste.getActiveWithPaging(avlevering.getPasientjournal(), uriInfo);
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
    public Response nyPasientjournal(@PathParam("id") String avleveringid, PersondataDTO person) throws ParseException {
        // VALIDERING - kun grunnopplysninger
        ArrayList<Valideringsfeil> valideringsfeil = 
                new Validator<PersondataDTO>(PersondataDTO.class, person).valider();
        valideringsfeil.addAll(DatoValiderer.valider(person));

        if(!valideringsfeil.isEmpty()) {
            throw new ValideringsfeilException(valideringsfeil);
        }
        
        //KONVERTERING
        Pasientjournal pasientjournal = Konverterer.tilPasientjournal(person);
        
        //SETTER VERDIER
        pasientjournal.setUuid(UUID.randomUUID().toString());
        
        //Setter visningsnavnet til det som er lagret i databasen for kjønn med koden
        if(person.getKjonn() != null) {
            Kjønn k = (Kjønn) kjønnTjeneste.getSingleInstance(person.getKjonn()).getEntity();
            pasientjournal.getGrunnopplysninger().setKjønn(k);
        }

        //LAGRER
        getEntityManager().persist(pasientjournal);
        Avlevering avlevering = (Avlevering) getSingleInstance(avleveringid).getEntity();
        avlevering.getPasientjournal().add(pasientjournal);
        return Response.ok().entity(pasientjournal).build();
    }
    
    @GET
    @Path("/{id}/leveranse")
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
        Avlevering avlevering = (Avlevering) getSingleInstance(id).getEntity();
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
