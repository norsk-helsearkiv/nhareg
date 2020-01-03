package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.TransferDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface TransferServiceInterface {

    Avlevering create(final TransferDTO transferDTO, final String username);

    TransferDTO update(final TransferDTO transferDTO, final String username);
    
    Avlevering delete(final String id);
    
    Avlevering getById(final String id);

    Avlevering getTransferForStorageUnit(final String id);

    List<TransferDTO> getAll(final MultivaluedMap<String, String> queryParameters);

    void lockTransfer(final String id);

    void unlockTransfer(final String id);

    Avlevering getDefaultTransfer(final String username);
    
}