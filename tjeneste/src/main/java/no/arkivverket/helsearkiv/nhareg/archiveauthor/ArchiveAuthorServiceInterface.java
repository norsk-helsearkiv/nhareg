package no.arkivverket.helsearkiv.nhareg.archiveauthor;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.ArchiveAuthorDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Set;

public interface ArchiveAuthorServiceInterface {
    
    ArchiveAuthorDTO getByName(final String name);

    Set<ArchiveAuthorDTO> getAll(final MultivaluedMap<String, String> queryParameters);

    ArchiveAuthorDTO create(final ArchiveAuthorDTO archiveAuthorDTO);

    ArchiveAuthorDTO update(final ArchiveAuthorDTO archiveAuthorDTO);

    ArchiveAuthorDTO getByCode(final String code);

    void delete(final String id);
    
}