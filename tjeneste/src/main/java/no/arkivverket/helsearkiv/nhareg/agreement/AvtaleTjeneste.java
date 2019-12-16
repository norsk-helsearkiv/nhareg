package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.common.Roller;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.AvleveringDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferResource;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferServiceInterface;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Path("/avtaler")
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class AvtaleTjeneste {

    @Inject
    private AgreementServiceInterface agreementService;
    
    @Inject
    private TransferServiceInterface transferService;
    
    @EJB
    private TransferResource transferResource;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Avtale> getAll(@Context UriInfo uriInfo) {
        return agreementService.getAll(uriInfo.getQueryParameters());
    }

    @GET
    @Path("/default")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDefaultAgreementId() {
        final Avlevering transfer = transferService.getDefaultTransfer();
        
        if (transfer == null) {
            return null;
        }
        
        return transfer.getAvtale().getAvtaleidentifikator();
    }

    @GET
    @Path("/{id}/avleveringer")
    public Response getTransfers(@PathParam("id") String id) {
        final List<AvleveringDTO> transferDTOList = agreementService.getTransfersById(id);
        return Response.ok(transferDTOList).build();
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
    @RolesAllowed(value = {Roller.ROLE_ADMIN})
    @Override
    public Avtale create(Avtale avtale) {
        //Henter virksomhet
        List<Virksomhet> virksomheter = getEntityManager()
                .createQuery("SELECT v FROM Virksomhet v")
                .getResultList();
        //Setter virksomhet
        avtale.setVirksomhet(virksomheter.get(0));

        Avtale other = getEntityManager().find(Avtale.class, avtale.getAvtaleidentifikator());
        if (other != null) {
            throw new EntityExistsException("Avtale med samme Id eksisterer");
        }
        
        //Oppretter avtale
        return super.create(avtale);
    }
    
    @DELETE
    @Path("/{id}")
    @RolesAllowed(value = {Roller.ROLE_ADMIN})
    @Override
    public Avtale delete(@PathParam("id") String id) {
        Avtale avtale = getSingleInstance(id);
        
        // Hent antall barn
        String jpql = "SELECT count(a) FROM Avlevering a WHERE a.avtale = :avtale";
        Query q = super.getEntityManager().createQuery(jpql);
        q.setParameter("avtale", avtale);
        Long antall = (Long) q.getSingleResult();
        
        // Slett om det ikke er barn
        if (antall == 0) {
            getEntityManager().remove(avtale);
            return avtale;
        } 
        
        ArrayList<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();
        valideringsfeil.add(new Valideringsfeil("Avtale", "HasChildren"));
        throw new ValideringsfeilException(valideringsfeil);
    }
}
