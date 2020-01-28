package no.arkivverket.helsearkiv.nhareg.business;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.BusinessDTO;

import javax.inject.Inject;

public class BusinessService implements BusinessServiceInterface {
    
    @Inject
    private BusinessDAO businessDAO;
    
    @Override
    public BusinessDTO getBusiness() {
        final Business business = businessDAO.fetchBusiness();
        
        return new BusinessDTO(business.getOrganizationNumber(),
                               business.getName(),
                               business.getBusinessName());
    }
}
