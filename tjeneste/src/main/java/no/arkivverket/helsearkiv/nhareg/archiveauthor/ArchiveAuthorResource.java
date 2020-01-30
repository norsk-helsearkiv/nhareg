package no.arkivverket.helsearkiv.nhareg.archiveauthor;

import no.arkivverket.helsearkiv.nhareg.auth.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.ArchiveAuthorDTO;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Set;

@Stateless
@Path("/authors")
@RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
public class ArchiveAuthorResource {
    
    @Inject
    private ArchiveAuthorServiceInterface authorService;
    
    @GET
    @Path("/{code}/code")
    public ArchiveAuthorDTO getByCode(@PathParam("code") final String code) {
        return authorService.getByCode(code);
    }
    
    @GET
    @Path("/{name}/name")
    public ArchiveAuthorDTO getByName(@PathParam("name") final String name) {
        return authorService.getByName(name);
    }
    
    @GET
    @Path("/all")
    public Set<ArchiveAuthorDTO> getAll(@Context UriInfo uriInfo) {
        return authorService.getAll(uriInfo.getQueryParameters());
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ArchiveAuthorDTO create(final ArchiveAuthorDTO archiveAuthorDTO) {
        return authorService.create(archiveAuthorDTO);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ArchiveAuthorDTO update(final ArchiveAuthorDTO archiveAuthorDTO) {
        return authorService.update(archiveAuthorDTO);
    }
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final String id) {
        authorService.delete(id);
        
        return Response.ok().build();
    }
    
}