package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.business.BusinessDAO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
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
    public AgreementDTO create(final AgreementDTO agreementDTO) {
        if (agreementDTO.getBusiness() == null) {
            final Business business = businessDAO.fetchBusiness();
            agreementDTO.setBusiness(business);
        }

        final Agreement agreement = AgreementConverter.convertToAgreement(agreementDTO);
        final Agreement newAgreement = agreementDAO.create(agreement);
        
        return AgreementConverter.convertToAgreementDTO(newAgreement); 
    }

    @Override
    public AgreementDTO delete(final String id) {
        final Agreement deleted = agreementDAO.delete(id);
        
        return AgreementConverter.convertToAgreementDTO(deleted);
    }

    @Override
    public AgreementDTO update(final AgreementDTO agreementDTO) {
        final Agreement agreement = AgreementConverter.convertToAgreement(agreementDTO);
        final Agreement updated = agreementDAO.update(agreement);
        
        return AgreementConverter.convertToAgreementDTO(updated);
    }

    @Override
    public List<AgreementDTO> getAll(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedQueries = ParameterConverter.multivaluedToMap(queryParameters);
        final List<Agreement> agreements = agreementDAO.fetchAll(mappedQueries);

        return AgreementConverter.convertToAgreementDTOList(agreements);
    }

    @Override
    public List<TransferDTO> getTransfersByAgreementId(final String id, final Transfer defaultTransfer) {
        final List<Transfer> transferList = agreementDAO.fetchTransfersByAgreementId(id);

        final List<TransferDTO> transferDTOS = new ArrayList<>();
        for (Transfer transfer : transferList) {
            TransferDTO transferDTO = new TransferDTO(transfer);
            if (defaultTransfer != null) {
                if (transfer.getTransferId().equals(defaultTransfer.getTransferId())) {
                    transferDTO.setDefaultTransfer(true);
                }
            }
            transferDTOS.add(transferDTO);
        }

        return transferDTOS; 
    }
    
}