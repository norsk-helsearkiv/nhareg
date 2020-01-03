package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.util.ParameterConverter;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AgreementService implements AgreementServiceInterface {

    @Inject
    private AgreementDAO agreementDAO;

    @Override
    public Avtale create(final Avtale agreement) {
        return agreementDAO.create(agreement);
    }

    @Override
    public Avtale delete(final String id) {
        return agreementDAO.delete(id);
    }

    @Override
    public List<Avtale> getAll(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedQueries = ParameterConverter.multivaluedToMap(queryParameters);
        return agreementDAO.fetchAll(mappedQueries);
    }

    @Override
    public List<TransferDTO> getTransfersById(final String id, final Avlevering defaultTransfer) {
        final List<Avlevering> transferList = agreementDAO.fetchTransfersById(id);

        final List<TransferDTO> transferDTOS = new ArrayList<>();
        for (Avlevering transfer : transferList) {
            TransferDTO transferDTO = new TransferDTO(transfer);
            if (defaultTransfer != null) {
                if (transfer.getAvleveringsidentifikator().equals(defaultTransfer.getAvleveringsidentifikator())) {
                    transferDTO.setDefaultAvlevering(true);
                }
            }
            transferDTOS.add(transferDTO);
        }

        return transferDTOS; 
    }
    
}