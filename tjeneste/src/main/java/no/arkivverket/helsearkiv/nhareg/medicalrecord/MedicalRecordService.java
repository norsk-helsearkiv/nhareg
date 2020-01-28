package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.business.BusinessDAO;
import no.arkivverket.helsearkiv.nhareg.common.ParameterConverter;
import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.*;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.PersonalDataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.Validator;
import no.arkivverket.helsearkiv.nhareg.gender.GenderDAO;
import no.arkivverket.helsearkiv.nhareg.storageunit.StorageUnitDAO;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferDAO;
import no.arkivverket.helsearkiv.nhareg.user.UserDAO;
import no.arkivverket.helsearkiv.nhareg.validation.DateValidation;
import no.arkivverket.helsearkiv.nhareg.validation.FanearkidValidation;
import no.arkivverket.helsearkiv.nhareg.validation.PIDValidation;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MedicalRecordService implements MedicalRecordServiceInterface {

    @Inject
    private MedicalRecordDAO medicalRecordDAO;

    @Inject
    private TransferDAO transferDAO;

    @Inject
    private BusinessDAO businessDAO;

    @Inject
    private StorageUnitDAO storageUnitDAO;

    @Inject
    private ConfigurationDAO configurationDAO;

    @Inject
    private GenderDAO genderDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private MedicalRecordConverterInterface medicalRecordConverter;

    @Override
    public MedicalRecord create(final MedicalRecord medicalRecord, final String username) {
        medicalRecord.setUuid(UUID.randomUUID().toString());

        if (medicalRecord.getGender() != null) {
            final Gender genderCode = medicalRecord.getGender();
            final Gender gender = genderDAO.fetchById(genderCode.getCode());
            medicalRecord.setGender(gender);
        }

        createAndAttachStorageUnit(medicalRecord.getStorageUnit());

        medicalRecord.setUpdateInfo(createUpdateInfo(username));
        medicalRecord.setCreatedDate(LocalDateTime.now());

        return medicalRecordDAO.create(medicalRecord);
    }

    /**
     * "Deletes" a medical record in the database by setting the "slettet" value to true.
     * @param id Id for the medical record to be deleted.
     * @return The updated medical record.
     */
    @Override
    public MedicalRecord delete(final String id, final String username) {
        final MedicalRecord medicalRecord = medicalRecordDAO.fetchSingleInstance(id);
        medicalRecord.setDeleted(true);
        medicalRecord.setUpdateInfo(createUpdateInfo(username));

        return medicalRecordDAO.update(medicalRecord);
    }

    @Override
    public MedicalRecord getById(final String id) {
        return medicalRecordDAO.fetchById(id);
    }

    @Override
    public MedicalRecordDTO getByIdWithTransfer(final String id) {
        final MedicalRecord medicalRecord = medicalRecordDAO.fetchById(id);
        final Transfer transfer = transferDAO.fetchTransferFromRecordId(id);
        final Business business = businessDAO.fetchBusiness();

        return medicalRecordConverter.toMedicalRecordDTO(medicalRecord, transfer, business.getBusinessName());
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
    public MedicalRecordDTO updateMedicalRecord(final MedicalRecordDTO medicalRecordDTO, final String username) {
        // Validation - Personal data
        validateBaseData(medicalRecordDTO.getPersonalDataDTO());

        // Converting
        final MedicalRecord medicalRecord = medicalRecordConverter.fromPersonalDataDTO(medicalRecordDTO.getPersonalDataDTO());
        createAndAttachStorageUnit(medicalRecord.getStorageUnit());

        final MedicalRecord original = medicalRecordDAO.fetchById(medicalRecord.getUuid());
        if (original != null) {
            medicalRecord.getDiagnosis().addAll(original.getDiagnosis());
        }

        if (medicalRecordDTO.getPersonalDataDTO().getGender() != null) {
            final Gender gender = genderDAO.fetchSingleInstance(medicalRecordDTO.getPersonalDataDTO().getGender());
            medicalRecord.setGender(gender);
        }

        // Update storageUnit
        final StorageUnit storageUnit = medicalRecord.getStorageUnit().iterator().next();
        final String storageUnitId = storageUnit.getId();
        if (storageUnitId != null && !storageUnitId.isEmpty()) {
            userDAO.updateStorageUnit(username, storageUnit.getId());
        }

        // Update transfer
        final String transferId = medicalRecordDTO.getTransferId();
        final Transfer transfer = transferDAO.fetchById(transferId);
        final String transferDescription = medicalRecordDTO.getTransferDescription();
        if (transferDescription != null && !transferDescription.isEmpty()) {
            transfer.setTransferDescription(transferDescription);
        }

        final String storageUnitFormat = medicalRecordDTO.getStorageUnitFormat();
        if (storageUnitFormat != null && !storageUnitFormat.isEmpty()) {
            transfer.setStorageUnitFormat(storageUnitFormat);
        }

        // Save transfer
        transferDAO.update(transfer);

        medicalRecord.setUpdateInfo(createUpdateInfo(username));
        if (original != null) {
            medicalRecord.setCreatedDate(original.getCreatedDate());
        }

        // Save record
        final MedicalRecord updatedMedicalRecord = medicalRecordDAO.update(medicalRecord);
        final String business = businessDAO.fetchBusiness().getBusinessName();

        return medicalRecordConverter.toMedicalRecordDTO(updatedMedicalRecord, transfer, business);
    }

    @Override
    public void validatePID(final String pid) {
        final ValidationError validationError = PIDValidation.validate(pid);
        if (validationError != null) {
            throw new ValidationErrorException(Collections.singleton(validationError));
        }
    }

    @Override
    public MedicalRecordDTO createInTransfer(final String transferId,
                                             final PersonalDataDTO personalDataDTO,
                                             final String username) {
        // Validate personal data
        validateBaseData(personalDataDTO);

        // Convert personal data to medical record and create medical record.
        final MedicalRecord fromPersonalDataDTO = medicalRecordConverter.fromPersonalDataDTO(personalDataDTO);
        final MedicalRecord medicalRecord = this.create(fromPersonalDataDTO, username);

        final Transfer transfer = transferDAO.fetchSingleInstance(transferId);
        transfer.getMedicalRecords().add(medicalRecord);

        // Tracking.
        transfer.setUpdateInfo(createUpdateInfo(username));
        final Business business = businessDAO.fetchBusiness();
        final MedicalRecordDTO medicalRecordDTO = medicalRecordConverter.toMedicalRecordDTO(medicalRecord,
                                                                                            transfer,
                                                                                            business.getBusinessName());
        final String uuid = medicalRecord.getUuid();
        final Transfer transferFromRecord = transferDAO.fetchTransferFromRecordId(uuid);
        medicalRecordDTO.setTransferId(transferFromRecord.getTransferId());

        // Update the users last used storage unit
        final StorageUnit storageUnit = medicalRecord.getStorageUnit().iterator().next();
        final String storageUnitId = storageUnit.getId();
        if (storageUnitId != null && !storageUnitId.isEmpty()) {
            userDAO.updateStorageUnit(username, storageUnitId);
        }

        return medicalRecordDTO;
    }

    private void validateBaseData(final PersonalDataDTO personalDataDTO) {
        // VALIDERING - Persondata
        final ArrayList<ValidationError> validationError =
            new Validator<>(PersonalDataDTO.class, personalDataDTO).validate();

        //Validerer forholdet mellom dataoer
        final DateValidation dateValidation = new DateValidation();
        validationError.addAll(dateValidation.validate(personalDataDTO, configurationDAO));

        final ValidationError pidError = PIDValidation.validate(personalDataDTO);
        if (pidError != null) {
            if (!validationError.contains(pidError)) {
                validationError.add(pidError);
            }
        }
    
        final Integer fieldLength = configurationDAO.getInt(ConfigurationDAO.CONFIG_FANEARKID);
        final ValidationError fanearkidError = FanearkidValidation.validate(personalDataDTO, fieldLength);
        if (fanearkidError != null) {
            validationError.add(fanearkidError);
        }

        if (validationError.size() > 0) {
            throw new ValidationErrorException(validationError);
        }
    }

    private void createAndAttachStorageUnit(final Set<StorageUnit> storageUnits) {
        // Create new storage units, ignores existing units
        storageUnits.forEach(storageUnitDAO::create);

        final List<StorageUnit> existingStorageUnits = storageUnits.stream()
                                                                   .map(unit -> storageUnitDAO.fetchById(unit.getId()))
                                                                   .collect(Collectors.toList());
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