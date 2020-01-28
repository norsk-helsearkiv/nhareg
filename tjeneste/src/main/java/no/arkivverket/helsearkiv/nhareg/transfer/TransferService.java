package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.agreement.AgreementConverterInterface;
import no.arkivverket.helsearkiv.nhareg.archivecreator.ArchiveCreatorDAO;
import no.arkivverket.helsearkiv.nhareg.common.ParameterConverter;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveCreator;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.UpdateInfo;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferInAgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.Validator;
import no.arkivverket.helsearkiv.nhareg.user.UserDAO;

import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.MultivaluedMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransferService implements TransferServiceInterface {
    
    @Inject
    private TransferDAO transferDAO;
    
    @Inject
    private UserDAO userDAO;
    
    @Inject
    private BusinessDAO businessDAO;
    
    @Inject
    private TransferConverterInterface transferConverter;
    
    @Inject
    private AgreementConverterInterface agreementConverter;
    
    @Inject
    private ArchiveCreatorDAO archiveCreatorDAO;

    @Override
    public TransferDTO create(final TransferDTO transferDTO, final String username) {
        final ArchiveCreator archiveCreator = archiveCreatorDAO.fetchByName(transferDTO.getArchiveCreator());
        final Transfer transfer = transferConverter.toTransfer(transferDTO, archiveCreator);
        transfer.setUpdateInfo(createUpdateInfo(username));
        transfer.setDateGenerated(LocalDate.now());
        
        try {
            final Transfer created = transferDAO.create(transfer);
            
            return transferConverter.fromTransfer(created);
        } catch (EJBTransactionRolledbackException ejb) { // Catch duplicate entries
            Throwable cause = ejb.getCause();

            while ((cause != null) && !(cause instanceof PersistenceException)) {
                cause = cause.getCause();
            }

            if (cause == null) {
                throw ejb;
            }
            
            throw new EntityExistsException("Avlevering med samme id eksisterer");
        }
    }

    @Override
    public TransferInAgreementDTO update(final TransferInAgreementDTO transferDTO, final String username) {
        // Validate
        new Validator<>(TransferInAgreementDTO.class).validateWithException(transferDTO);

        // Get existing transfer
        final Transfer existingTransfer = transferDAO.fetchById(transferDTO.getTransferId());

        // Copy values
        existingTransfer.setTransferId(transferDTO.getTransferId());
        existingTransfer.setTransferDescription(transferDTO.getTransferDescription());
        
        // Get the archive creator
        final String archiveCreatorString = transferDTO.getArchiveCreator();
        if (archiveCreatorString != null && !archiveCreatorString.isEmpty()) {
            final ArchiveCreator creator = archiveCreatorDAO.fetchByName(archiveCreatorString);
            
            if (creator != null) {
                existingTransfer.setArchiveCreator(creator);
            } else {
                int subEnd = Math.min(archiveCreatorString.length(), 3);
                final String archiveCreatorCode = archiveCreatorString.substring(0, subEnd);
                final ArchiveCreator archiveCreator = new ArchiveCreator(UUID.randomUUID().toString(), 
                                                                         archiveCreatorCode,
                                                                         archiveCreatorString,
                                                                         null);
                existingTransfer.setArchiveCreator(archiveCreator);
            }
        }
        
        existingTransfer.setStorageUnitFormat(transferDTO.getStorageUnitFormat());

        // Set update info
        existingTransfer.setUpdateInfo(createUpdateInfo(username));

        // Update
        final Transfer updatedTransfer = transferDAO.update(existingTransfer);
        final Business business = businessDAO.fetchBusiness();
        final AgreementDTO agreementDTO = agreementConverter.fromAgreement(updatedTransfer.getAgreement());

        return transferConverter.toInAgreementDTO(updatedTransfer, business, agreementDTO);
    }

    @Override
    public TransferDTO delete(final String id) {
        final Transfer deleted = transferDAO.delete(id);
        
        return transferConverter.fromTransfer(deleted);
    }

    @Override
    public TransferDTO getById(final String id) {
        final Transfer transfer = transferDAO.fetchById(id);
        
        return transferConverter.fromTransfer(transfer);
    }

    @Override
    public List<TransferDTO> getAll(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedQueries = ParameterConverter.multivaluedToMap(queryParameters);
        final List<Transfer> transferList = transferDAO.fetchAll(mappedQueries);
        
        // Convert to TransferDTO list
        return transferList.stream().map(transferConverter::fromTransfer).collect(Collectors.toList());
    }

    @Override 
    public TransferDTO getTransferForStorageUnit(final String id) {
        final Transfer transfer = transferDAO.fetchTransferForStorageUnit(id);
        
        return transferConverter.fromTransfer(transfer);
    }

    @Override
    public void lockTransfer(final String id) {
        final Transfer transfer = transferDAO.fetchById(id);
        transfer.setLocked(true);
        transferDAO.update(transfer);
    }

    @Override 
    public void unlockTransfer(final String id) {
        final Transfer transfer = transferDAO.fetchById(id);
        transfer.setLocked(false);
        transferDAO.update(transfer);
    }

    @Override
    public TransferDTO getDefaultTransfer(final String username) {
        final User user = userDAO.fetchByUsername(username);
        final String defaultUuid = user.getDefaultTransferId();

        if (defaultUuid == null || defaultUuid.isEmpty()) {
            return null;
        }

        final Transfer transfer = transferDAO.fetchById(defaultUuid);
        
        return transferConverter.fromTransfer(transfer);
    }

    private UpdateInfo createUpdateInfo(final String username) {
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setUpdatedBy(username);
        updateInfo.setLastUpdated(LocalDateTime.now());

        return updateInfo;
    }
    
}