package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.business.BusinessDAO;
import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.Validator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.gender.GenderDAO;
import no.arkivverket.helsearkiv.nhareg.storageunit.StorageUnitDAO;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferDAO;
import no.arkivverket.helsearkiv.nhareg.user.UserDAO;
import no.arkivverket.helsearkiv.nhareg.util.DatoValiderer;
import no.arkivverket.helsearkiv.nhareg.util.FanearkidValiderer;
import no.arkivverket.helsearkiv.nhareg.util.ParameterConverter;
import no.arkivverket.helsearkiv.nhareg.util.PersonnummerValiderer;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MultivaluedMap;
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
    public Pasientjournal create(final Pasientjournal medicalRecord, final String username) {
        medicalRecord.setUuid(UUID.randomUUID().toString());

        final Grunnopplysninger baseProperties = medicalRecord.getGrunnopplysninger();
        if (baseProperties != null) {
            if (baseProperties.getKjønn() != null) {
                Kjønn kjonn = baseProperties.getKjønn();
                kjonn = genderDAO.fetchById(kjonn.getCode());
                baseProperties.setKjønn(kjonn);
            }
        }

        createAndAttachStorageUnit(medicalRecord.getLagringsenhet());

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
    public Pasientjournal delete(final String id, final String username) {
        final Pasientjournal medicalRecord = medicalRecordDAO.fetchSingleInstance(id);
        medicalRecord.setSlettet(true);
        medicalRecord.setOppdateringsinfo(createUpdateInfo(username));

        return medicalRecordDAO.update(medicalRecord);
    }

    @Override
    public Pasientjournal getById(final String id) {
        return medicalRecordDAO.fetchById(id);
    }

    @Override
    public MedicalRecordDTO getByIdWithTransfer(final String id) {
        final Pasientjournal pasientjournal = medicalRecordDAO.fetchById(id);
        final Avlevering transfer = transferDAO.fetchTransferFromRecordId(id);
        final Virksomhet business = businessDAO.fetchBusiness();

        return MedicalRecordConverter.convertToMedicalRecordDTO(pasientjournal, transfer, business.getForetaksnavn());
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
        return new ListObject<>(recordTransferDTOList, recordTransferDTOList.size(), page, size);
    }
    
    @Override
    public MedicalRecordDTO updateMedicalRecord(final MedicalRecordDTO medicalRecordDTO, final String username) {
        // VALIDERING - Persondata
        validateBaseData(medicalRecordDTO.getPersondata());

        //KONVERTERING
        final Pasientjournal medicalRecord = MedicalRecordConverter.convertFromPersonalDataDTO(medicalRecordDTO.getPersondata());
        createAndAttachStorageUnit(medicalRecord.getLagringsenhet());
        
        final Pasientjournal original = medicalRecordDAO.fetchById(medicalRecord.getUuid());
        if (original != null) {
            medicalRecord.getDiagnose().addAll(original.getDiagnose());
        }

        //Setter verdier
        if (medicalRecordDTO.getPersondata().getKjonn() != null) {
            final Kjønn gender = genderDAO.fetchSingleInstance(medicalRecordDTO.getPersondata().getKjonn());
            medicalRecord.getGrunnopplysninger().setKjønn(gender);
        }

        // Update lagringsenhet
        Lagringsenhet lagringsenhet = medicalRecord.getLagringsenhet().get(0);
        final String storageUnitId = lagringsenhet.getIdentifikator();
        if (storageUnitId != null && !storageUnitId.isEmpty()) {
            userDAO.updateLagringsenhet(username, lagringsenhet.getIdentifikator());
        }

        // Update avlevering
        final Avlevering transfer = transferDAO.fetchById(medicalRecordDTO.getAvleveringsidentifikator());
        final String transferDescription = medicalRecordDTO.getAvleveringBeskrivelse();
        if (transferDescription != null && !transferDescription.isEmpty()) {
            transfer.setAvleveringsbeskrivelse(transferDescription);
        }

        final String lagringsenhetFormat = medicalRecordDTO.getLagringsenhetformat();
        if (lagringsenhetFormat != null && !lagringsenhetFormat.isEmpty()) {
            transfer.setLagringsenhetformat(lagringsenhetFormat);
        }
        
        transferDAO.update(transfer);

        // Save
        medicalRecord.setOppdateringsinfo(createUpdateInfo(username));
        medicalRecord.setOpprettetDato(original.getOpprettetDato());
        final Pasientjournal updatedMedicalRecord = medicalRecordDAO.update(medicalRecord);
        final String business = businessDAO.fetchBusiness().getForetaksnavn();

        return MedicalRecordConverter.convertToMedicalRecordDTO(updatedMedicalRecord, transfer, business);
    }

    @Override
    public void validatePID(final String pid) {
        Valideringsfeil fnrfeil = PersonnummerValiderer.valider(pid);
        if (fnrfeil != null) {
            throw new ValideringsfeilException(Collections.singleton(fnrfeil));
        }
    }

    @Override
    public MedicalRecordDTO createInTransfer(final String transferId, 
                                             final PersondataDTO personalDataDTO,
                                             final String username) {
        // Validate personal data
        validateBaseData(personalDataDTO);

        // Convert personal data to medical record
        final Pasientjournal medicalRecord = MedicalRecordConverter.convertFromPersonalDataDTO(personalDataDTO); 

        // Save
        medicalRecordDAO.create(medicalRecord);
        final Avlevering transfer = transferDAO.fetchSingleInstance(transferId);
        transfer.getPasientjournal().add(medicalRecord);

        // Tracking.
        transfer.setOppdateringsinfo(createUpdateInfo(username));
        final Virksomhet business = businessDAO.fetchBusiness();
        final MedicalRecordDTO medicalRecordDTO = MedicalRecordConverter.convertToMedicalRecordDTO(medicalRecord,
                                                                                                   transfer,
                                                                                                   business.getForetaksnavn());
        final String transferIdForRecord = transferDAO.fetchTransferIdFromRecordId(medicalRecord.getUuid());
        medicalRecordDTO.setAvleveringsidentifikator(transferIdForRecord);

        // Update the users last used storage unit
        final Lagringsenhet lagringsenhet = medicalRecord.getLagringsenhet().get(0);
        final String storageUnitId = lagringsenhet.getIdentifikator();
        if (storageUnitId != null && !storageUnitId.isEmpty()) {
            userDAO.updateLagringsenhet(username, storageUnitId);
        }

        return medicalRecordDTO;
    }

    private void validateBaseData(final PersondataDTO persondataDTO) {
        // VALIDERING - Persondata
        ArrayList<Valideringsfeil> valideringsfeil = new Validator<>(PersondataDTO.class, persondataDTO).valider();

        //Validerer forholdet mellom dataoer
        DatoValiderer datoValiderer = new DatoValiderer();
        valideringsfeil.addAll(datoValiderer.valider(persondataDTO, configurationDAO));

        Valideringsfeil fnrfeil = PersonnummerValiderer.valider(persondataDTO);
        if (fnrfeil != null) {
            if (!valideringsfeil.contains(fnrfeil)) {
                valideringsfeil.add(fnrfeil);
            }
        }

        Valideringsfeil fanearkidFeil = FanearkidValiderer.valider(persondataDTO, configurationDAO);
        if (fanearkidFeil != null) {
            valideringsfeil.add(fanearkidFeil);
        }

        if (valideringsfeil.size() > 0) {
            throw new ValideringsfeilException(valideringsfeil);
        }
    }

    private void createAndAttachStorageUnit(final List<Lagringsenhet> storageUnits) {
        // Create new storage units
        try {
            storageUnits.forEach(storageUnitDAO::create);
        } catch (ConstraintViolationException ignored) {
            // Ignore existing ones 
        }
        
        final List<Lagringsenhet> existingStorageUnits = storageUnits.stream()
                                                                     .map(unit -> storageUnitDAO.fetchById(unit.getIdentifikator()))
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