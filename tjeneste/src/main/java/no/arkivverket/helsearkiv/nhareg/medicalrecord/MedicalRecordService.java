package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.common.ParameterConverter;
import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.UpdateInfo;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.Validator;
import no.arkivverket.helsearkiv.nhareg.storageunit.StorageUnitDAO;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferDAO;
import no.arkivverket.helsearkiv.nhareg.user.UserDAO;
import no.arkivverket.helsearkiv.nhareg.validation.DateValidation;
import no.arkivverket.helsearkiv.nhareg.validation.FanearkidValidation;
import no.arkivverket.helsearkiv.nhareg.validation.PIDValidation;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.MultivaluedMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MedicalRecordService implements MedicalRecordServiceInterface {

    @Inject
    private MedicalRecordDAO medicalRecordDAO;

    @Inject
    private TransferDAO transferDAO;

    @Inject
    private StorageUnitDAO storageUnitDAO;

    @Inject
    private ConfigurationDAO configurationDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private MedicalRecordConverterInterface medicalRecordConverter;

    @Override
    public MedicalRecordDTO create(final MedicalRecordDTO medicalRecordDTO, final String username) {
        // Validate data
        validateBaseData(medicalRecordDTO);

        // Get the associated transfer
        final String transferId = medicalRecordDTO.getTransferId();
        final Transfer transfer = transferDAO.fetchSingleInstance(transferId);
        validateTransfer(transfer);

        // Convert DTO to medical record and create medical record.
        final MedicalRecord medicalRecord = medicalRecordConverter.fromMedicalRecordDTO(medicalRecordDTO);
        final String uuid = UUID.randomUUID().toString();
        medicalRecord.setUuid(uuid);

        createAndAttachStorageUnits(medicalRecord.getStorageUnits());

        medicalRecord.setUpdateInfo(createUpdateInfo(username));
        medicalRecord.setCreatedDate(LocalDateTime.now());
        medicalRecord.setTransfer(transfer);

        // Save record
        medicalRecordDAO.create(medicalRecord);

        // Add record to transfer
        transfer.getMedicalRecords().add(medicalRecord);
        transfer.setUpdateInfo(createUpdateInfo(username));

        final MedicalRecordDTO createdRecord = medicalRecordConverter.toMedicalRecordDTO(medicalRecord);
        createdRecord.setTransferId(transfer.getTransferId());
        createdRecord.setTransferLocked(transfer.isLocked());

        // Update the users last used storage unit
        final StorageUnit storageUnit = medicalRecord.getStorageUnits().iterator().next();
        final String storageUnitId = storageUnit.getId();
        if (storageUnitId != null && !storageUnitId.isEmpty()) {
            userDAO.updateStorageUnit(username, storageUnitId);
        }

        return createdRecord;
    }

    @Override
    public MedicalRecordDTO update(final MedicalRecordDTO medicalRecordDTO, final String username) {
        // Validation
        validateBaseData(medicalRecordDTO);

        // Check if associated transfer is locked.
        final Transfer transfer = transferDAO.fetchTransferFromRecordId(medicalRecordDTO.getUuid());
        validateTransfer(transfer);

        // Converting
        final MedicalRecord medicalRecord = medicalRecordConverter.fromMedicalRecordDTO(medicalRecordDTO);
        createAndAttachStorageUnits(medicalRecord.getStorageUnits());
        medicalRecord.setUpdateInfo(createUpdateInfo(username));
        medicalRecord.setTransfer(transfer);

        // Fetch original diagnoses. This is done because of the cascading properties of medical records diagnoses.
        // I.E. not doing this will result in all diagnoses not part of the update being deleted.
        final MedicalRecord original = medicalRecordDAO.fetchById(medicalRecord.getUuid());
        if (original != null) {
            medicalRecord.getDiagnosis().addAll(original.getDiagnosis());
        }

        // Save record
        final MedicalRecord updatedMedicalRecord = medicalRecordDAO.update(medicalRecord);

        return medicalRecordConverter.toMedicalRecordDTO(updatedMedicalRecord);
    }

    /**
     * "Deletes" a medical record in the database by setting the "slettet" value to true.
     * @param id Id for the medical record to be deleted.
     * @return The updated medical record.
     */
    @Override
    public MedicalRecordDTO delete(final String id, final String username) {
        final Transfer transfer = transferDAO.fetchTransferFromRecordId(id);
        validateTransfer(transfer);

        final MedicalRecord medicalRecord = medicalRecordDAO.fetchById(id);
        if (medicalRecord == null) {
            throw new NoResultException(id);
        }

        medicalRecord.setDeleted(true);
        medicalRecord.setFanearkid(null);
        medicalRecord.setUpdateInfo(createUpdateInfo(username));

        final MedicalRecord deleted = medicalRecordDAO.update(medicalRecord);

        return medicalRecordConverter.toMedicalRecordDTO(deleted);
    }

    @Override
    public MedicalRecordDTO getById(final String id) {
        return medicalRecordConverter.toMedicalRecordDTO(medicalRecordDAO.fetchById(id));
    }

    @Override
    public MedicalRecordDTO getByIdWithTransfer(final String id) {
        final MedicalRecord medicalRecord = medicalRecordDAO.fetchById(id);

        return medicalRecordConverter.toMedicalRecordDTO(medicalRecord);
    }

    @Override
    public ListObject getAllWithTransfers(final MultivaluedMap<String, String> queryParameters, final String id) {
        final Map<String, String> mappedQueries = ParameterConverter.multivaluedToMap(queryParameters);
        final String pageString = mappedQueries.remove("page");
        final String sizeString = mappedQueries.remove("size");
        int page = 0;
        int size = 0;

        if (pageString != null && sizeString != null) {
            page = Integer.parseInt(pageString);
            size = Integer.parseInt(sizeString);
        }

        if (id != null && !id.isEmpty()) {
            mappedQueries.put("transferId", id);
        }

        final List<MedicalRecord> recordList = medicalRecordDAO.fetchAllRecordTransfers(mappedQueries, page, size);
        final int totalSize = medicalRecordDAO.fetchAllRecordTransferCount(mappedQueries);
        final List<RecordTransferDTO> recordTransferDTOList =
            new ArrayList<>(medicalRecordConverter.toRecordTransferDTOList(recordList));

        return new ListObject<>(recordTransferDTOList, totalSize, page, size);
    }

    @Override
    public void validatePID(final String pid) {
        final ValidationError validationError = PIDValidation.validate(pid);
        if (validationError != null) {
            throw new ValidationErrorException(Collections.singleton(validationError));
        }
    }

    private void validateTransfer(final Transfer transfer) {
        final List<ValidationError> validationErrors = new ArrayList<>();

        if (transfer == null) {
            validationErrors.add(new ValidationError("Avlevering", "MissingTransfer"));
        } else if (transfer.isLocked()) {
            validationErrors.add(new ValidationError("Avlevering", "IsLocked"));
        }

        if (validationErrors.size() > 0) {
            throw new ValidationErrorException(validationErrors);
        }
    }

    private void validateBaseData(final MedicalRecordDTO medicalRecordDTO) {
        final ArrayList<ValidationError> validationErrors =
            new Validator<>(MedicalRecordDTO.class, medicalRecordDTO).validate();

        final DateValidation dateValidation = new DateValidation();
        final LocalDate lowLim = configurationDAO.getDate(ConfigurationDAO.CONFIG_LOWLIM);
        final Integer waitLim = configurationDAO.getInt(ConfigurationDAO.CONFIG_WAITLIM);
        final Integer maxAge = configurationDAO.getInt(ConfigurationDAO.CONFIG_MAXAGE);
        validationErrors.addAll(dateValidation.validate(medicalRecordDTO, lowLim, waitLim, maxAge));

        final ValidationError pidError = PIDValidation.validate(medicalRecordDTO);
        if (pidError != null) {
            if (!validationErrors.contains(pidError)) {
                validationErrors.add(pidError);
            }
        }

        final Integer fieldLength = configurationDAO.getInt(ConfigurationDAO.CONFIG_FANEARKID);
        final ValidationError fanearkidError = FanearkidValidation.validate(medicalRecordDTO, fieldLength);
        if (fanearkidError != null) {
            validationErrors.add(fanearkidError);
        }

        // Validate that fanearkid is unique.
        final Long fanearkid = medicalRecordDTO.getFanearkid();
        if (fanearkid != null) {
            final MedicalRecord duplicate = medicalRecordDAO.fetchByFanearkid(fanearkid.toString());
            if (duplicate != null && !duplicate.getUuid().equals(medicalRecordDTO.getUuid())) {
                validationErrors.add(new ValidationError("fanearkid", "NotUnique"));
            }
        }
 
        if (validationErrors.size() > 0) {
            throw new ValidationErrorException(validationErrors);
        }
    }

    private void createAndAttachStorageUnits(final Set<StorageUnit> storageUnits) {
        // Create new storage units, ignores existing units
        storageUnits.forEach(storageUnitDAO::create);

        final Set<StorageUnit> existingStorageUnits = storageUnits.stream()
                                                                  .map(unit -> storageUnitDAO.fetchById(unit.getId()))
                                                                  .collect(Collectors.toSet());
        storageUnits.clear();
        storageUnits.addAll(existingStorageUnits);
    }

    private UpdateInfo createUpdateInfo(final String username) {
        final UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setUpdatedBy(username);
        updateInfo.setLastUpdated(LocalDateTime.now());

        return updateInfo;
    }

}