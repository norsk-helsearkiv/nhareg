package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Oppdateringsinfo;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.Validator;
import no.arkivverket.helsearkiv.nhareg.user.UserDAO;
import no.arkivverket.helsearkiv.nhareg.util.ParameterConverter;

import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransferService implements TransferServiceInterface {
    
    @Inject
    private TransferDAO transferDAO;
    
    @Inject
    private UserDAO userDAO;

    @Override
    public TransferDTO create(final TransferDTO transferDTO, final String username) {
        final Transfer transfer = transferDTO.toTransfer();
        transfer.setOppdateringsinfo(createUpdateInfo(username));

        try {
            final Transfer created = transferDAO.create(transfer);
            
            return new TransferDTO(created);
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
    public TransferDTO update(final TransferDTO transferDTO, final String username) {
        // Validate
        new Validator<>(TransferDTO.class).validateWithException(transferDTO);

        // Get existing transfer
        final Transfer existingTransfer = transferDAO.fetchById(transferDTO.getTransferId());

        // Copy values
        existingTransfer.setTransferId(transferDTO.getTransferId());
        existingTransfer.setTransferDescription(transferDTO.getTransferDescription());
        existingTransfer.setArkivskaper(transferDTO.getArchiveCreator());
        existingTransfer.setStorageUnitFormat(transferDTO.getStorageUnitFormat());

        // Set update info
        existingTransfer.setOppdateringsinfo(createUpdateInfo(username));

        // Update
        final Transfer updatedTransfer = transferDAO.update(existingTransfer);

        return new TransferDTO(updatedTransfer);
    }

    @Override
    public TransferDTO delete(final String id) {
        final Transfer deleted = transferDAO.delete(id);
        
        return new TransferDTO(deleted);
    }

    @Override
    public TransferDTO getById(final String id) {
        final Transfer transfer = transferDAO.fetchById(id);
        
        return new TransferDTO(transfer);
    }

    @Override
    public List<TransferDTO> getAll(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedQueries = ParameterConverter.multivaluedToMap(queryParameters);
        final List<Transfer> transferList = transferDAO.fetchAll(mappedQueries);
        
        // Convert to TransferDTO list
        return transferList.stream().map(TransferDTO::new).collect(Collectors.toList());
    }

    @Override 
    public TransferDTO getTransferForStorageUnit(final String id) {
        final Transfer transfer = transferDAO.fetchTransferForStorageUnit(id);
        
        return new TransferDTO(transfer);
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
        final String defaultUuid = user.getDefaultAvleveringsUuid();

        if (defaultUuid == null || defaultUuid.isEmpty()) {
            return null;
        }

        final Transfer transfer = transferDAO.fetchById(defaultUuid);
        
        return new TransferDTO(transfer);
    }

    private Oppdateringsinfo createUpdateInfo(final String username) {
        Oppdateringsinfo oppdateringsinfo = new Oppdateringsinfo();
        oppdateringsinfo.setOppdatertAv(username);
        oppdateringsinfo.setSistOppdatert(Calendar.getInstance());

        return oppdateringsinfo;
    }
    
}