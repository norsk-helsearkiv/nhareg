package no.arkivverket.helsearkiv.nhareg.archiveauthor;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveAuthor;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.ArchiveAuthorDTO;

import java.util.Collection;
import java.util.Set;

public interface ArchiveAuthorConverterInterface {
    
    ArchiveAuthorDTO fromArchiveAuthor(final ArchiveAuthor archiveAuthor);
    
    Set<ArchiveAuthorDTO> fromArchiveAuthorCollection(final Collection<ArchiveAuthor> archiveAuthors);

    ArchiveAuthor toArchiveAuthor(final ArchiveAuthorDTO archiveAuthorDTO);
    
    Set<ArchiveAuthor> toArchiveAuthorSet(final Set<ArchiveAuthorDTO> archiveAuthorDTOSet);
    
}