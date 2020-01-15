package no.arkivverket.helsearkiv.nhareg.archivecreator;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveCreator;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Stateless
public class ArchiveCreatorDAO extends EntityDAO<ArchiveCreator> {

    public ArchiveCreatorDAO() {
        super(ArchiveCreator.class, "kode");
    }
    
    public ArchiveCreator fetchByName(final String name) {
        final String queryString = "SELECT OBJECT(ac) " 
            + "FROM ArchiveCreator ac " 
            + "WHERE ac.name = :name ";
        
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("name", name);
        
        try {
            return (ArchiveCreator) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    
}