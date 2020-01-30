package no.arkivverket.helsearkiv.nhareg.archiveauthor;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveAuthor;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Stateless
public class ArchiveAuthorDAO extends EntityDAO<ArchiveAuthor> {

    public ArchiveAuthorDAO() {
        super(ArchiveAuthor.class, "uuid");
    }

    @Override
    public ArchiveAuthor create(final ArchiveAuthor archiveAuthor) {
        super.create(archiveAuthor);
        getEntityManager().flush();

        return archiveAuthor;
    }

    @Override
    public ArchiveAuthor update(final ArchiveAuthor archiveAuthor) {
        final ArchiveAuthor updated = super.update(archiveAuthor);
        getEntityManager().flush();

        return updated;
    }

    @Override
    public ArchiveAuthor delete(final String id) {
        final ArchiveAuthor delete = super.delete(id);
        getEntityManager().flush();
        
        return delete;
    }

    public ArchiveAuthor fetchByName(final String name) {
        final String queryString = "SELECT ac "
            + "FROM ArchiveAuthor ac "
            + "WHERE ac.name = :name ";

        final TypedQuery<ArchiveAuthor> query = getEntityManager().createQuery(queryString, ArchiveAuthor.class);
        query.setParameter("name", name);

        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public ArchiveAuthor fetchByCode(final String code) {
        final String queryString = "SELECT ac "
            + "FROM ArchiveAuthor ac "
            + "WHERE ac.code = :code ";
        final TypedQuery<ArchiveAuthor> query = getEntityManager().createQuery(queryString, ArchiveAuthor.class);
        query.setParameter("code", code);

        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}