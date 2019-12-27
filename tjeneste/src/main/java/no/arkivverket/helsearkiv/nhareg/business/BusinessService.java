package no.arkivverket.helsearkiv.nhareg.business;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;

import javax.inject.Inject;

public class BusinessService implements BusinessServiceInterface {
    
    @Inject
    private BusinessDAO businessDAO;
    
    @Override
    public Virksomhet getBusiness() {
        return businessDAO.fetchBusiness();
    }
}
