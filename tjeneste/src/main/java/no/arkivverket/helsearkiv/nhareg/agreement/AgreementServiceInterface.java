package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.TransferDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface AgreementServiceInterface {
    
    List<AgreementDTO> getAll(final MultivaluedMap<String, String> queryParameters);

    List<TransferDTO> getTransfersByAgreementId(final String id, final Transfer defaultTransfer);

    AgreementDTO create(final AgreementDTO agreementDTO);

    AgreementDTO delete(final String id);

    AgreementDTO update(final AgreementDTO agreementDTO);
}