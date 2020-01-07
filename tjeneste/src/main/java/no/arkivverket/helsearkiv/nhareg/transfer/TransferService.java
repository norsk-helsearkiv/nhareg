package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Oppdateringsinfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.Validator;
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
    public Avlevering create(final TransferDTO transferDTO, final String username) {
        final Avlevering transfer = transferDTO.toTransfer();
        transfer.setOppdateringsinfo(createUpdateInfo(username));

        try {
            return transferDAO.create(transfer);
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
        new Validator<>(TransferDTO.class).validerMedException(transferDTO);

        // Get existing transfer
        final Avlevering existingTransfer = transferDAO.fetchById(transferDTO.getAvleveringsidentifikator());

        // Copy values
        existingTransfer.setAvleveringsidentifikator(transferDTO.getAvleveringsidentifikator());
        existingTransfer.setAvleveringsbeskrivelse(transferDTO.getAvleveringsbeskrivelse());
        existingTransfer.setArkivskaper(transferDTO.getArkivskaper());
        existingTransfer.setLagringsenhetformat(transferDTO.getLagringsenhetformat());

        // Set update info
        existingTransfer.setOppdateringsinfo(createUpdateInfo(username));

        // Update
        final Avlevering updatedTransfer = transferDAO.update(existingTransfer);

        return new TransferDTO(updatedTransfer);
    }

    @Override
    public Avlevering delete(final String id) {
        return transferDAO.delete(id);
    }

    @Override
    public Avlevering getById(final String id) {
        return transferDAO.fetchById(id);
    }

    @Override
    public List<TransferDTO> getAll(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedQueries = ParameterConverter.multivaluedToMap(queryParameters);
        final List<Avlevering> transferList = transferDAO.fetchAll(mappedQueries);
        
        // Convert to TransferDTO list
        return transferList.stream().map(TransferDTO::new).collect(Collectors.toList());
    }

    @Override 
    public Avlevering getTransferForStorageUnit(final String id) {
        return transferDAO.fetchTransferForStorageUnit(id);
    }

    @Override
    public void lockTransfer(final String id) {
        final Avlevering transfer = transferDAO.fetchById(id);
        transfer.setLaast(true);
        transferDAO.update(transfer);
    }

    @Override 
    public void unlockTransfer(final String id) {
        final Avlevering transfer = transferDAO.fetchById(id);
        transfer.setLaast(false);
        transferDAO.update(transfer);
    }

    @Override
    public Avlevering getDefaultTransfer(final String username) {
        final Bruker bruker = userDAO.fetchByUsername(username);
        final String defaultUuid = bruker.getDefaultAvleveringsUuid();

        if (defaultUuid == null || defaultUuid.isEmpty()) {
            return null;
        }
        
        return transferDAO.fetchById(defaultUuid);
    }

    private Oppdateringsinfo createUpdateInfo(final String username) {
        Oppdateringsinfo oppdateringsinfo = new Oppdateringsinfo();
        oppdateringsinfo.setOppdatertAv(username);
        oppdateringsinfo.setSistOppdatert(Calendar.getInstance());

        return oppdateringsinfo;
    }
    
}