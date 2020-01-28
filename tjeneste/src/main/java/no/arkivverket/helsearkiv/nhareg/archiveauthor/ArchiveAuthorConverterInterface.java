package no.arkivverket.helsearkiv.nhareg.archiveauthor;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveAuthor;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.ArchiveAuthorDTO;

import java.util.List;

public interface ArchiveAuthorConverterInterface {
    
    ArchiveAuthorDTO fromArchiveAuthor(final ArchiveAuthor archiveAuthor);
    
    List<ArchiveAuthorDTO> fromArchiveAuthorList(final List<ArchiveAuthor> archiveAuthors);

    ArchiveAuthor toArchiveAuthor(final ArchiveAuthorDTO archiveAuthorDTO);
}