package no.arkivverket.helsearkiv.nhareg.delivery;

import javax.inject.Inject;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;

public class DeliveryService implements DeliveryServiceInterface {

    @Inject
    private DeliveryDAO deliveryDAO;
    
    @Override
    public Avlevering getAvlevering(final String id) {
        return deliveryDAO.getById(id);
    }
}
