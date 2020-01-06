package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.TransferDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface AgreementServiceInterface {
    
    List<Agreement> getAll(final MultivaluedMap<String, String> queryParameters);

    List<TransferDTO> getTransfersByAgreementId(final String id, final Avlevering defaultTransfer);

    Agreement create(final Agreement agreement);

    Agreement delete(final String id);

    Agreement update(final Agreement agreement);
}