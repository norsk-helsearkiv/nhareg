package no.arkivverket.helsearkiv.nhareg.business;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;

import javax.inject.Inject;

public class BusinessService implements BusinessServiceInterface {
    
    @Inject
    private BusinessDAO businessDAO;
    
    @Override
    public Business getBusiness() {
        return businessDAO.fetchBusiness();
    }
}
