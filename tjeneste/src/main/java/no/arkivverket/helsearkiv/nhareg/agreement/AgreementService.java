package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.business.BusinessDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;
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

    @Inject
    private BusinessDAO businessDAO;
    
    @Override
    public Agreement create(final Agreement agreement) {
        if (agreement.getVirksomhet() == null) {
            final Virksomhet business = businessDAO.fetchBusiness();
            agreement.setVirksomhet(business);
        }
        
        return agreementDAO.create(agreement);
    }

    @Override
    public Agreement delete(final String id) {
        return agreementDAO.delete(id);
    }

    @Override
    public Agreement update(final Agreement agreement) {
        return agreementDAO.update(agreement);
    }

    @Override
    public List<Agreement> getAll(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedQueries = ParameterConverter.multivaluedToMap(queryParameters);
        return agreementDAO.fetchAll(mappedQueries);
    }

    @Override
    public List<TransferDTO> getTransfersByAgreementId(final String id, final Avlevering defaultTransfer) {
        final List<Avlevering> transferList = agreementDAO.fetchTransfersByAgreementId(id);

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