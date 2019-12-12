package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;

import javax.inject.Inject;

public class TransferService implements TransferServiceInterface {

    @Inject
    private TransferDAO transferDAO;

    @Override
    public Avlevering getAvlevering(final String id) {
        return transferDAO.fetchById(id);
    }

}