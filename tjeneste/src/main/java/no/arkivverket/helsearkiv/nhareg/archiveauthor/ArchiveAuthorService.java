package no.arkivverket.helsearkiv.nhareg.archiveauthor;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveAuthor;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.ArchiveAuthorDTO;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

import static no.arkivverket.helsearkiv.nhareg.common.ParameterConverter.multivaluedToMap;

public class ArchiveAuthorService implements ArchiveAuthorServiceInterface {
    
    @Inject
    private ArchiveAuthorDAO archiveAuthorDAO;

    @Inject
    private ArchiveAuthorConverterInterface archiveAuthorConverter;
    
    @Override
    public ArchiveAuthorDTO getByName(final String name) {
        final ArchiveAuthor author = archiveAuthorDAO.fetchByName(name);
        
        return archiveAuthorConverter.fromArchiveAuthor(author);
    }

    @Override 
    public List<ArchiveAuthorDTO> getAll(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedQueries = multivaluedToMap(queryParameters);
        List<ArchiveAuthor> archiveAuthors = archiveAuthorDAO.fetchAll(mappedQueries);
        
        return archiveAuthorConverter.fromArchiveAuthorList(archiveAuthors);
    }

    @Override 
    public ArchiveAuthorDTO create(final ArchiveAuthorDTO archiveAuthorDTO) {
        final ArchiveAuthor archiveAuthor = archiveAuthorConverter.toArchiveAuthor(archiveAuthorDTO);
        final ArchiveAuthor newAuthor = archiveAuthorDAO.create(archiveAuthor);
        
        return archiveAuthorConverter.fromArchiveAuthor(newAuthor);
    }

    @Override
    public ArchiveAuthorDTO update(final ArchiveAuthorDTO archiveAuthorDTO) {
        final ArchiveAuthor author = archiveAuthorConverter.toArchiveAuthor(archiveAuthorDTO);
        final ArchiveAuthor updated = archiveAuthorDAO.update(author);
        
        return archiveAuthorConverter.fromArchiveAuthor(updated);
    }

    @Override
    public ArchiveAuthorDTO getByCode(final String code) {
        final ArchiveAuthor archiveAuthor = archiveAuthorDAO.fetchByCode(code);
        
        return archiveAuthorConverter.fromArchiveAuthor(archiveAuthor);
    }

    @Override
    public void delete(final String id) {
        archiveAuthorDAO.delete(id);
    }

}