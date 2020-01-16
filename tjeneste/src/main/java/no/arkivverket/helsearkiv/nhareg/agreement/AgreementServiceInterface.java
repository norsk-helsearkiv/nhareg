package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferInAgreementDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface AgreementServiceInterface {
    
    AgreementDTO create(final AgreementDTO agreementDTO);

    AgreementDTO delete(final String id);

    AgreementDTO update(final AgreementDTO agreementDTO);

    List<AgreementDTO> getAll(final MultivaluedMap<String, String> queryParameters);

    List<TransferInAgreementDTO> getTransfersByAgreementId(final String id, final Transfer defaultTransfer);
    
}