package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.business.BusinessDAO;
import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Validator;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.gender.GenderDAO;
import no.arkivverket.helsearkiv.nhareg.storageunit.StorageUnitDAO;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferDAO;
import no.arkivverket.helsearkiv.nhareg.user.UserDAO;
import no.arkivverket.helsearkiv.nhareg.util.ParameterConverter;
import no.arkivverket.helsearkiv.nhareg.validation.DateValidation;
import no.arkivverket.helsearkiv.nhareg.validation.FanearkidValidation;
import no.arkivverket.helsearkiv.nhareg.validation.PIDValidation;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.math.BigInteger;
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

    @Override
    public MedicalRecord create(final MedicalRecord medicalRecord, final String username) {
        medicalRecord.setUuid(UUID.randomUUID().toString());

        final Grunnopplysninger baseProperties = medicalRecord.getGrunnopplysninger();
        if (baseProperties != null) {
            if (baseProperties.getGender() != null) {
                Gender gender = baseProperties.getGender();
                gender = genderDAO.fetchById(gender.getCode());
                baseProperties.setGender(gender);
            }
        }

        createAndAttachStorageUnit(medicalRecord.getStorageUnit());

        medicalRecord.setOppdateringsinfo(createUpdateInfo(username));
        medicalRecord.setOpprettetDato(Calendar.getInstance());

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
        medicalRecord.setSlettet(true);
        medicalRecord.setOppdateringsinfo(createUpdateInfo(username));

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
        final Virksomhet business = businessDAO.fetchBusiness();

        return MedicalRecordConverter.convertToMedicalRecordDTO(medicalRecord, transfer, business.getForetaksnavn());
    }

    @Override
    public ListObject getAllWithTransfers(final MultivaluedMap<String, String> queryParameters) {
        int page = 0;
        int size = 0;

        if (queryParameters.containsKey("size") && queryParameters.containsKey("page")) {
            page = Integer.parseInt(queryParameters.getFirst("page"));
            size = Integer.parseInt(queryParameters.getFirst("size"));
        }

        final Map<String, String> mappedQueries = ParameterConverter.multivaluedToMap(queryParameters);
        final List<RecordTransferDTO> recordTransferDTOList = medicalRecordDAO.fetchAllRecordTransfers(mappedQueries);
        final BigInteger totalSize = medicalRecordDAO.fetchAllRecordTransferCount(mappedQueries);
        return new ListObject<>(recordTransferDTOList, totalSize.intValueExact(), page, size);
    }

    @Override
    public MedicalRecordDTO updateMedicalRecord(final MedicalRecordDTO medicalRecordDTO, final String username) {
        // VALIDERING - Persondata
        validateBaseData(medicalRecordDTO.getPersonalDataDTO());

        //KONVERTERING
        final MedicalRecord medicalRecord = MedicalRecordConverter.convertFromPersonalDataDTO(medicalRecordDTO.getPersonalDataDTO());
        createAndAttachStorageUnit(medicalRecord.getStorageUnit());

        final MedicalRecord original = medicalRecordDAO.fetchById(medicalRecord.getUuid());
        if (original != null) {
            medicalRecord.getDiagnosis().addAll(original.getDiagnosis());
        }

        //Setter verdier
        if (medicalRecordDTO.getPersonalDataDTO().getGender() != null) {
            final Gender gender = genderDAO.fetchSingleInstance(medicalRecordDTO.getPersonalDataDTO().getGender());
            medicalRecord.getGrunnopplysninger().setGender(gender);
        }

        // Update storageUnit
        StorageUnit storageUnit = medicalRecord.getStorageUnit().get(0);
        final String storageUnitId = storageUnit.getId();
        if (storageUnitId != null && !storageUnitId.isEmpty()) {
            userDAO.updateLagringsenhet(username, storageUnit.getId());
        }

        // Update avlevering
        final String transferId = medicalRecordDTO.getTransferId();
        final Transfer transfer = transferDAO.fetchById(transferId);
        final String transferDescription = medicalRecordDTO.getTransferDescription();
        if (transferDescription != null && !transferDescription.isEmpty()) {
            transfer.setTransferDescription(transferDescription);
        }

        final String lagringsenhetFormat = medicalRecordDTO.getStorageUnitFormat();
        if (lagringsenhetFormat != null && !lagringsenhetFormat.isEmpty()) {
            transfer.setStorageUnitFormat(lagringsenhetFormat);
        }

        transferDAO.update(transfer);

        // Save
        medicalRecord.setOppdateringsinfo(createUpdateInfo(username));
        if (original != null) {
            medicalRecord.setOpprettetDato(original.getOpprettetDato());
        }
        
        final MedicalRecord updatedMedicalRecord = medicalRecordDAO.update(medicalRecord);
        final String business = businessDAO.fetchBusiness().getForetaksnavn();

        return MedicalRecordConverter.convertToMedicalRecordDTO(updatedMedicalRecord, transfer, business);
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
                                             final PersondataDTO personalDataDTO,
                                             final String username) {
        // Validate personal data
        validateBaseData(personalDataDTO);

        // Convert personal data to medical record and create medical record.
        final MedicalRecord fromPersonalDataDTO = MedicalRecordConverter.convertFromPersonalDataDTO(personalDataDTO);
        final MedicalRecord medicalRecord = this.create(fromPersonalDataDTO, username);
        
        final Transfer transfer = transferDAO.fetchSingleInstance(transferId);
        transfer.getMedicalRecords().add(medicalRecord);

        // Tracking.
        transfer.setOppdateringsinfo(createUpdateInfo(username));
        final Virksomhet business = businessDAO.fetchBusiness();
        final MedicalRecordDTO medicalRecordDTO = MedicalRecordConverter.convertToMedicalRecordDTO(medicalRecord,
                                                                                                   transfer,
                                                                                                   business.getForetaksnavn());
        final String transferIdForRecord = transferDAO.fetchTransferIdFromRecordId(medicalRecord.getUuid());
        medicalRecordDTO.setTransferId(transferIdForRecord);

        // Update the users last used storage unit
        final StorageUnit storageUnit = medicalRecord.getStorageUnit().get(0);
        final String storageUnitId = storageUnit.getId();
        if (storageUnitId != null && !storageUnitId.isEmpty()) {
            userDAO.updateLagringsenhet(username, storageUnitId);
        }

        return medicalRecordDTO;
    }

    private void validateBaseData(final PersondataDTO persondataDTO) {
        // VALIDERING - Persondata
        ArrayList<ValidationError> validationError = new Validator<>(PersondataDTO.class, persondataDTO).validate();

        //Validerer forholdet mellom dataoer
        DateValidation dateValidation = new DateValidation();
        validationError.addAll(dateValidation.validate(persondataDTO, configurationDAO));

        ValidationError fnrfeil = PIDValidation.validate(persondataDTO);
        if (fnrfeil != null) {
            if (!validationError.contains(fnrfeil)) {
                validationError.add(fnrfeil);
            }
        }

        ValidationError fanearkidFeil = FanearkidValidation.validate(persondataDTO, configurationDAO);
        if (fanearkidFeil != null) {
            validationError.add(fanearkidFeil);
        }

        if (validationError.size() > 0) {
            throw new ValidationErrorException(validationError);
        }
    }

    private void createAndAttachStorageUnit(final List<StorageUnit> storageUnits) {
        // Create new storage units, ignores existing units
        storageUnits.forEach(storageUnitDAO::create);

        final List<StorageUnit> existingStorageUnits = storageUnits.stream()
                                                                   .map(unit -> storageUnitDAO.fetchById(unit.getId()))
                                                                   .collect(Collectors.toList());
        storageUnits.clear();
        storageUnits.addAll(existingStorageUnits);
    }

    private Oppdateringsinfo createUpdateInfo(final String username) {
        Oppdateringsinfo oppdateringsinfo = new Oppdateringsinfo();
        oppdateringsinfo.setOppdatertAv(username);
        oppdateringsinfo.setSistOppdatert(Calendar.getInstance());

        return oppdateringsinfo;
    }

}