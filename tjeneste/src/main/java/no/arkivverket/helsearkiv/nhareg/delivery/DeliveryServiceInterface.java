package no.arkivverket.helsearkiv.nhareg.delivery;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;

public interface DeliveryServiceInterface {

    Avlevering getAvlevering(final String id);
    
}
