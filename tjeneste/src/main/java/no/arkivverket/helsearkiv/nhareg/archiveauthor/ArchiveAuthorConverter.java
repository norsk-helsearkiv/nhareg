package no.arkivverket.helsearkiv.nhareg.archiveauthor;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveAuthor;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.ArchiveAuthorDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ArchiveAuthorConverter implements ArchiveAuthorConverterInterface {
    
    @Override
    public ArchiveAuthorDTO fromArchiveAuthor(final ArchiveAuthor archiveAuthor) {
        if (archiveAuthor == null) {
            return null;
        }
        
        return new ArchiveAuthorDTO(archiveAuthor.getUuid(), archiveAuthor.getCode(), archiveAuthor.getName(), 
                                    archiveAuthor.getDescription());
    }

    @Override
    public List<ArchiveAuthorDTO> fromArchiveAuthorList(final List<ArchiveAuthor> archiveAuthors) {
        if (archiveAuthors == null) {
            return null;
        }
        
        return archiveAuthors.stream().map(this::fromArchiveAuthor).collect(Collectors.toList());
    }

    @Override
    public ArchiveAuthor toArchiveAuthor(final ArchiveAuthorDTO archiveAuthorDTO) {
        if (archiveAuthorDTO == null) {
            return null;
        }
        
        return new ArchiveAuthor(archiveAuthorDTO.getUuid(), archiveAuthorDTO.getCode(), archiveAuthorDTO.getName(),
                                 archiveAuthorDTO.getDescription());
    }

}