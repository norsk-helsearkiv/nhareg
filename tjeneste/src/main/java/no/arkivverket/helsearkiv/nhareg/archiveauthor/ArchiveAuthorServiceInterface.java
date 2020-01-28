package no.arkivverket.helsearkiv.nhareg.archiveauthor;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.ArchiveAuthorDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface ArchiveAuthorServiceInterface {
    
    ArchiveAuthorDTO getByName(final String name);

    List<ArchiveAuthorDTO> getAll(final MultivaluedMap<String, String> queryParameters);

    ArchiveAuthorDTO create(final ArchiveAuthorDTO archiveAuthorDTO);

    ArchiveAuthorDTO update(final ArchiveAuthorDTO archiveAuthorDTO);

    ArchiveAuthorDTO getByCode(final String code);
    
}