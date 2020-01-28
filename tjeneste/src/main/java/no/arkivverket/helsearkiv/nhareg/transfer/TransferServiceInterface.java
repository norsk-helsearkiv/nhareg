package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface TransferServiceInterface {

    TransferDTO create(final TransferDTO transferDTO, final String username);

    TransferDTO update(final TransferDTO transferDTO, final String username);
    
    TransferDTO delete(final String id);
    
    TransferDTO getById(final String id);

    Transfer getTransferById(final String id);
    
    TransferDTO getTransferForStorageUnit(final String id);

    List<TransferDTO> getAll(final MultivaluedMap<String, String> queryParameters);

    TransferDTO lockTransfer(final String id);

    TransferDTO unlockTransfer(final String id);

    TransferDTO getDefaultTransfer(final String username);
    
}