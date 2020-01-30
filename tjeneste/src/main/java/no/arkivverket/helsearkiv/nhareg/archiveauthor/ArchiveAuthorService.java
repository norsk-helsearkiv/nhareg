package no.arkivverket.helsearkiv.nhareg.archiveauthor;

import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveAuthor;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.ArchiveAuthorDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.util.*;

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
    public Set<ArchiveAuthorDTO> getAll(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedQueries = multivaluedToMap(queryParameters);
        final List<ArchiveAuthor> archiveAuthors = archiveAuthorDAO.fetchAll(mappedQueries);

        return new HashSet<>(archiveAuthorConverter.fromArchiveAuthorCollection(archiveAuthors));
    }

    @Override
    public ArchiveAuthorDTO create(final ArchiveAuthorDTO archiveAuthorDTO) {
        final ArchiveAuthor archiveAuthor = archiveAuthorConverter.toArchiveAuthor(archiveAuthorDTO);
        archiveAuthor.setUuid(UUID.randomUUID().toString());
        validateAuthor(archiveAuthor);
        
        final ArchiveAuthor newAuthor = archiveAuthorDAO.create(archiveAuthor);

        return archiveAuthorConverter.fromArchiveAuthor(newAuthor);
    }

    @Override
    public ArchiveAuthorDTO update(final ArchiveAuthorDTO archiveAuthorDTO) {
        final ArchiveAuthor author = archiveAuthorConverter.toArchiveAuthor(archiveAuthorDTO);
        validateAuthor(author);
        
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
    
    private void validateAuthor(final ArchiveAuthor author) {
        final List<ValidationError> validationErrors = new ArrayList<>();

        if (author.getCode() == null || author.getCode().isEmpty()) {
            validationErrors.add(new ValidationError("code", "NotNull"));
        }
        
        if (author.getName() == null || author.getName().isEmpty()) {
            validationErrors.add(new ValidationError("name", "NotNull"));
        }
        
        final ArchiveAuthor codeAuthor = archiveAuthorDAO.fetchByCode(author.getCode());
        if (codeAuthor != null && !codeAuthor.getUuid().equals(author.getUuid())) {
            validationErrors.add(new ValidationError("code", "NotUnique"));
        }

        final ArchiveAuthor nameAuthor = archiveAuthorDAO.fetchByName(author.getName());
        if (nameAuthor != null && !nameAuthor.getUuid().equals(author.getUuid())) {
            validationErrors.add(new ValidationError("name", "NotUnique"));
        }
        
        if (validationErrors.size() > 0) {
            throw new ValidationErrorException(validationErrors);
        }
    }

}