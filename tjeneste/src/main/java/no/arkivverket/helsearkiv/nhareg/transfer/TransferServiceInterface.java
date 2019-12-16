package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.AvleveringDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface TransferServiceInterface {

    Avlevering create(final AvleveringDTO transferDTO);

    AvleveringDTO update(final AvleveringDTO transferDTO);
    
    Avlevering delete(final String id);
    
    Avlevering getById(final String id);

    Avlevering getTransferForStorageUnit(final String id);

    String getFirstTransferId(final String id);

    String getTransferId(final String medicalRecordId);

    List<AvleveringDTO> getAll(final MultivaluedMap<String, String> queryParameters);

    void lockTransfer(final String id);

    void unlockTransfer(final String id);

    Avlevering getDefaultTransfer();
    
}