package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;

public interface TransferServiceInterface {

    Avlevering getAvlevering(final String id);

}