package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.AvleveringDTO;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferDAO;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgreementService implements AgreementServiceInterface {

    @Inject
    private AgreementDAO agreementDAO;

    @Inject
    private TransferDAO transferDAO;
    
    @Override
    public List<Avtale> getAll(MultivaluedMap<String, String> queryParameters) {
        return agreementDAO.fetchAll(convertToMap(queryParameters));
    }

    @Override
    public List<AvleveringDTO> getTransfersById(final String id, final Avlevering defaultTransfer) {
        final List<Avlevering> transferList = agreementDAO.fetchTransfersById(id);

        final List<AvleveringDTO> dtoListe = new ArrayList<AvleveringDTO>();
        for (Avlevering avlevering : transferList) {
            AvleveringDTO dto = new AvleveringDTO(avlevering);
            if (defaultTransfer != null) {
                if (avlevering.getAvleveringsidentifikator().equals(defaultTransfer.getAvleveringsidentifikator())) {
                    dto.setDefaultAvlevering(true);
                }
            }
            dtoListe.add(dto);
        }


        return dtoListe; 
    }

    /**
     * Filter out empty entries and convert to map.
     * @param queryParameters HTTP query parameters passed to the endpoint.
     * @return HashMap all empty params removed.
     */
    private Map<String, String> convertToMap(final MultivaluedMap<String, String> queryParameters) {
        Map<String, String> mappedQueries = new HashMap<>();
        // Convert to map
        queryParameters.forEach((key, value) -> mappedQueries.put(key, value.get(0)));
        // Remove all empty entries, as well as page and size
        mappedQueries.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue().isEmpty());
        return mappedQueries;
    }
}
