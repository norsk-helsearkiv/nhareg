package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
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
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.Validator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.util.DatoValiderer;
import no.arkivverket.helsearkiv.nhareg.util.DiagnoseTilDTOTransformer;
import no.arkivverket.helsearkiv.nhareg.util.Konverterer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Transformer;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Pasientjournal}r. Arver metodene
 * fra {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/pasientjournaler")
/**
 * <p>
 * Dette er en stateless service, vi deklarer den som EJB for å få
 * transaksjonsstøtte.
 * </p>
 */
@Stateless
public class PasientjournalTjeneste extends EntitetsTjeneste<Pasientjournal, String> {

    @EJB
    private KjønnTjeneste kjønnTjeneste;
    @EJB
    DiagnoseTjeneste diagnoseTjeneste;

    @EJB(name = "DiagnoseDTOTransformer")
    Transformer<DiagnoseDTO, Diagnose> diagnoseDTOTransformer;

    Transformer<Diagnose, DiagnoseDTO> diagnoseTilDTOTransformer = new DiagnoseTilDTOTransformer();

    public PasientjournalTjeneste() {
        super(Pasientjournal.class, String.class, "uuid");
    }

    /**
     * Returnerer pasientjournaler basert på søk i query parmeter med paging
     *
     * @param uriInfo, sok=string, first=int, max=int
     * @return Liste av pasientjournaler med avlevering
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getAll(@Context UriInfo uriInfo) {
        // Legg til søk i stedet for getAll(queryParameter). Skal returnere liste av treff
        // Sender in et tomt map for å få alle
        List<Pasientjournal> pasientjournaler = getAll(new MultivaluedHashMap<String, String>());
        return Response.ok(getActiveWithPaging(pasientjournaler, uriInfo)).build();
    }

    /**
     * Pasientjournal has soft delete, this methods removes the inactive and
     * returns the number of active pasientjournals.
     *
     * @param pasientjournaler
     * @param uriInfo
     * @return ListeObjekt
     */
    public ListeObjekt getActiveWithPaging(List<Pasientjournal> pasientjournaler, UriInfo uriInfo) {
        //Begrenser antallet som skal returneres til paging
        int total = pasientjournaler.size();
        int forste = 0;
        int side = 1;
        int antall = total;

        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        if (queryParameters.containsKey("side") && queryParameters.containsKey("antall")) {
            Integer qSide = Integer.parseInt(queryParameters.getFirst("side"));
            Integer qAntall = Integer.parseInt(queryParameters.getFirst("antall"));

            if (qSide > 0 && qAntall > 0) {
                side = qSide;
                antall = qAntall;
                forste = (side - 1) * antall;
            }
        }

        List<PasientjournalSokeresultatDTO> resultatListe = new ArrayList<PasientjournalSokeresultatDTO>();
        int totalAktive = 0;
        int antallIListe = 0;

        for (int i = 0; i < total; i++) {
            //Aktiv
            if (pasientjournaler.get(i).isSlettet() == null
                    || !pasientjournaler.get(i).isSlettet()) {

                if (antallIListe < antall && i >= forste) {
                    resultatListe.add(Konverterer.tilPasientjournalSokeresultatDTO(pasientjournaler.get(i)));
                    antallIListe++;
                }
                totalAktive++;
            }
        }

        //Setter Avtale og Avlevering på resultatobjektene - begrenset liste
        for (PasientjournalSokeresultatDTO obj : resultatListe) {
            //Finner avleveringen
            String jpql = "SELECT distinct a FROM Avlevering a inner join a.pasientjournal p WHERE p.uuid = :id";
            Query q = getEntityManager().createQuery(jpql);
            q.setParameter("id", obj.getUuid());
            Avlevering a = (Avlevering) q.getSingleResult();
            if (a != null) {
                obj.setAvlevering(a.getAvleveringsbeskrivelse());
                obj.setAvtale(a.getAvtale().getAvtalebeskrivelse());
            }
        }
        return new ListeObjekt(resultatListe, totalAktive, side, antall);
    }

    @GET
    @Path("/{id}")
    @Override
    public Response getSingleInstance(@PathParam("id") String id) {
        Pasientjournal pasientjournal = super.hent(id);
        if (pasientjournal == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        PasientjournalDTO pasientjournalDTO = Konverterer.tilPasientjournalDTO(pasientjournal);
        //
        // Legger til diagnoser.
        //
        Collection<DiagnoseDTO> diagnoseCollection = CollectionUtils.collect(pasientjournal.getDiagnose(), diagnoseTilDTOTransformer);
        pasientjournalDTO.setDiagnoser(new ArrayList<DiagnoseDTO>(diagnoseCollection));
        
        return Response.ok(pasientjournalDTO).build();
    }

    //får ikke brukt super sin update, for det er DTO som valideres, ikke Pasientjournal
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response oppdaterPasientjournal(PasientjournalDTO pasientjournalDTO) throws ParseException {
        // VALIDERING - Persondata
        ArrayList<Valideringsfeil> valideringsfeil
                = new Validator<PersondataDTO>(PersondataDTO.class, pasientjournalDTO.getPersondata()).valider();
        //Validerer forholdet mellom dataoer
        valideringsfeil.addAll(DatoValiderer.valider(pasientjournalDTO.getPersondata()));

        // VALIDERING - Diagnoser
        //Coming soon (tm)
        if (!valideringsfeil.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(valideringsfeil).build();
        }

        //KONVERTERING
        Pasientjournal pasientjournal = Konverterer.tilPasientjournal(pasientjournalDTO.getPersondata());
        //Legger til Diagnoser - Coming soon (tm)

        //Setter verdier
        if (pasientjournalDTO.getPersondata().getKjonn() != null) {
            Kjønn k = (Kjønn) kjønnTjeneste
                    .getSingleInstance(pasientjournalDTO.getPersondata()
                            .getKjonn())
                    .getEntity();
            pasientjournal.getGrunnopplysninger().setKjønn(k);
        }

        Pasientjournal persistert = getEntityManager().merge(pasientjournal);
        return Response.ok(persistert).build();
    }

    @DELETE
    @Path("/{id}")
    @Override
    public Response delete(@PathParam("id") String id) {
        Pasientjournal p = (Pasientjournal) super.getSingleInstance(id).getEntity();
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        p.setSlettet(true);
        getEntityManager().merge(p);
        return Response.ok().build();
    }

    /**
     * Legger til diagnose for pasientjournal
     *
     * @param id på pasientjournalen
     * @param diagnoseDTO
     * @param diagnose som skal legges til
     * @return 200 OK
     */
    @POST
    @Path("/{id}/diagnoser")
    public Response leggTilDiagnose(@PathParam("id") String id, DiagnoseDTO diagnoseDTO) {
        Pasientjournal pasientjournal = hent(id);
        if (pasientjournal == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Diagnose diagnose = diagnoseDTOTransformer.transform(diagnoseDTO);
        diagnoseTjeneste.create(diagnose);
        pasientjournal.getDiagnose().add(diagnose);
        return Response.ok().build();
    }

    /**
     * Fjerner diagnose fra pasientjournal
     *
     * @param id på pasientjournalen
     * @param diagnoseDTO som skal fjernes
     * @return 200 OK
     */
    @DELETE
    @Path("/{id}/diagnoser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fjernDiagnose(@PathParam("id") String id, DiagnoseDTO diagnoseDTO) {
        Pasientjournal pasientjournal = hent(id);
        if (pasientjournal == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Diagnose diagnose = diagnoseTjeneste.hent(diagnoseDTO.getUuid());
        if (diagnose == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        pasientjournal.getDiagnose().remove(diagnose);
        diagnoseTjeneste.delete(diagnose.getUuid());
        return Response.ok().build();
    }

    /**
     * Legger til vedlegg til pasientjournalen
     *
     * @param id på pasientjournalen
     * @param fil som skal legges til
     * @return vedleggsid
     */
    @POST
    @Path("/{id}/vedlegg")
    @Produces(MediaType.APPLICATION_JSON)
    public Response leggTilVedlegg(@PathParam("id") String id, File fil) {
        return Response.noContent().build();
    }

    /**
     * Sletter vedlegg fra JCR og fjerner vedlegget fra pasientjournalen
     *
     * @param pasientjournalId id på pasientjournalen
     * @param vedleggId id på vedlegget
     * @return 200 OK
     */
    @DELETE
    @Path("/{pid}/vedlegg/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fjernVedlegg(@PathParam("pid") String pasientjournalId, @PathParam("vid") String vedleggId) {
        return Response.noContent().build();
    }

    /*
     Dette endepunktet blir ikke brukt er,
     POST avleveringer/{id}/pasientjournaler
     */
    @Override
    public Response create(Pasientjournal entity) {
        entity.setUuid(UUID.randomUUID().toString());
        //
        if (entity.getGrunnopplysninger() != null) {
            Grunnopplysninger grunnopplysninger = entity.getGrunnopplysninger();
            if (grunnopplysninger.getKjønn() != null) {
                Kjønn kjønn = grunnopplysninger.getKjønn();
                kjønn = (Kjønn) kjønnTjeneste.getSingleInstance(kjønn.getCode()).getEntity();
                grunnopplysninger.setKjønn(kjønn);
            }
        }
        return super.create(entity);
    }

}
