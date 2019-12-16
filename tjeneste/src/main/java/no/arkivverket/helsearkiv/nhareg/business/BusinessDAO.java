package no.arkivverket.helsearkiv.nhareg.business;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class BusinessDAO {

    @PersistenceContext(name = "primary")
    private EntityManager entityManager;
    
    private static String GET_BUSINESS_LIST_QUERY = "SELECT v FROM Virksomhet v";
    
    public Virksomhet fetchBusiness() {
        final Query query  = entityManager.createQuery(GET_BUSINESS_LIST_QUERY, Virksomhet.class);
        final List<Virksomhet> businessList = query.getResultList();
        return businessList.get(0);
    }

}