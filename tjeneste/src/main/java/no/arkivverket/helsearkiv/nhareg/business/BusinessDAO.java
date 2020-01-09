package no.arkivverket.helsearkiv.nhareg.business;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class BusinessDAO {

    @PersistenceContext(name = "primary")
    private EntityManager entityManager;
    
    private static String GET_BUSINESS_LIST_QUERY = "SELECT v FROM Business v";
    
    public Business fetchBusiness() {
        final Query query  = entityManager.createQuery(GET_BUSINESS_LIST_QUERY, Business.class);
        final List<Business> businessList = query.getResultList();
        return businessList.get(0);
    }

}