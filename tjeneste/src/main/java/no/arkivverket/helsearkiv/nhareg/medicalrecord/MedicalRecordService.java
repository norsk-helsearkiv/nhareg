package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.business.BusinessDAO;
import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.gender.GenderDAO;
import no.arkivverket.helsearkiv.nhareg.storageunit.StorageUnitDAO;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferDAO;
import no.arkivverket.helsearkiv.nhareg.user.UserDAO;
import no.arkivverket.helsearkiv.nhareg.util.DatoValiderer;
import no.arkivverket.helsearkiv.nhareg.util.FanearkidValiderer;
import no.arkivverket.helsearkiv.nhareg.util.PersonnummerValiderer;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.util.*;
import java.util.stream.Collectors;

public class MedicalRecordService implements MedicalRecordServiceInterface {

    private static final String SIZE = "size";
    private static final String PAGE = "page";

    @Resource
    private SessionContext sessionContext;
    
    @Inject
    private MedicalRecordDAO medicalRecordDAO;
    
    @Inject
    private TransferDAO transferDAO;
    
    @Inject
    private BusinessDAO businessDAO;

    @Inject
    private StorageUnitDAO storageUnitDAO;
    
    @Inject
    private ConfigurationDAO konfigparam;
    
    @Inject
    private GenderDAO genderDAO;
    
    @Inject
    private UserDAO userDAO;

    @Override 
    public Pasientjournal create(final Pasientjournal medicalRecord) {
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

        medicalRecord.setOppdateringsinfo(createUpdateInfo());
        medicalRecord.setOpprettetDato(Calendar.getInstance());
        
        return medicalRecordDAO.create(medicalRecord);
    }

    /**
     * "Deletes" a medical record in the database by setting the "slettet" value to true.
     * @param id Id for the medical record to be deleted.
     * @return The updated medical record.
     */
    @Override
    public Pasientjournal delete(final String id) {
        final Pasientjournal medicalRecord = medicalRecordDAO.fetchSingleInstance(id);
        
        medicalRecord.getDiagnose().size();
        medicalRecord.setSlettet(true);
        medicalRecord.setOppdateringsinfo(createUpdateInfo());

        return medicalRecordDAO.update(medicalRecord);
    }

    @Override
    public Pasientjournal getById(final String id) {
        return medicalRecordDAO.fetchById(id);
    }

    @Override
    public MedicalRecordDTO getByIdWithTransfer(final String id) {
        final Pasientjournal pasientjournal = medicalRecordDAO.fetchById(id);
        final TransferDTO transferDTO = transferDAO.fetchTransferFromRecordId(id);
        final Virksomhet business = businessDAO.fetchBusiness();

        // TODO 
        return MedicalRecordConverter.convertToMedicalRecordDTO(pasientjournal, transferDTO, business.getForetaksnavn());
    }

    @Override
    public ListObject getAllWithTransfers(final MultivaluedMap<String, String> queryParameters) {
        int page = 0;
        int size  = 0;

        if (queryParameters.containsKey(PAGE) && queryParameters.containsKey(SIZE)) {
            page = Integer.parseInt(queryParameters.getFirst(PAGE));
            size = Integer.parseInt(queryParameters.getFirst(SIZE));
        }

        final Map<String, String> mappedQueries = convertToMap(queryParameters);
        final List<RecordTransferDTO> recordTransferDTOList = 
            medicalRecordDAO.fetchAllRecordTransfers(mappedQueries, page, size);
        return new ListObject<>(recordTransferDTOList, recordTransferDTOList.size(), page, size);
    }
    
    @Override
    public MedicalRecordDTO updateMedicalRecord(final MedicalRecordDTO medicalRecordDTO) {
        final MedicalRecordDTO updatedMedicalRecord = new MedicalRecordDTO();
        
        // VALIDERING - Persondata
        validateBaseData(medicalRecordDTO.getPersondata());

        //KONVERTERING
        final Pasientjournal medicalRecord = MedicalRecordConverter.convertFromPersonalDataDTO(medicalRecordDTO.getPersondata());

        createAndAttachStorageUnit(medicalRecord.getLagringsenhet());
        final Pasientjournal original = medicalRecordDAO.fetchById(medicalRecord.getUuid());
        if (original != null) {
            medicalRecord.getDiagnose().addAll(original.getDiagnose());
        }

        //pasientjournal.getDiagnose().addAll(CollectionUtils.collect(pasientjournalDTO.getDiagnoser(), diagnoseFraDTOTransformer));
        //Setter verdier
        if (medicalRecordDTO.getPersondata().getKjonn() != null) {
            Kjønn kjonn = genderDAO.fetchSingleInstance(medicalRecordDTO.getPersondata().getKjonn());
            medicalRecord.getGrunnopplysninger().setKjønn(kjonn);
        }

        // Update lagringsenhet
        Lagringsenhet lagringsenhet = medicalRecord.getLagringsenhet().get(0);
        final String username = sessionContext.getCallerPrincipal().getName();
        final String storageUnitId = lagringsenhet.getIdentifikator();
        if (storageUnitId != null && !storageUnitId.isEmpty()) {
            userDAO.updateLagringsenhet(username, lagringsenhet.getIdentifikator());
        }

        // Update avlevering
        Avlevering avlevering = transferDAO.fetchById(medicalRecordDTO.getAvleveringsidentifikator());
        final String beskrivelse = medicalRecordDTO.getAvleveringBeskrivelse();
        if (beskrivelse != null && !beskrivelse.isEmpty()) {
            avlevering.setAvleveringsbeskrivelse(beskrivelse);
        }

        final String lagringsenhetFormat = medicalRecordDTO.getLagringsenhetformat();
        if (lagringsenhetFormat != null && !lagringsenhetFormat.isEmpty()) {
            avlevering.setLagringsenhetformat(lagringsenhetFormat);
        }
        
        transferDAO.update(avlevering);

        // Save
        medicalRecord.setOppdateringsinfo(createUpdateInfo());
        medicalRecord.setOpprettetDato(original.getOpprettetDato());
        Pasientjournal oppdatertPasientjournal = medicalRecordDAO.update(medicalRecord);

        // return MedicalRecordMapper.convertToMedicalRecordDTO(oppdatertPasientjournal, transferDTO, businessDTO);
        return updatedMedicalRecord;
    }

    @Override
    public void validatePID(String pid) {
        Valideringsfeil fnrfeil = PersonnummerValiderer.valider(pid);
        if (fnrfeil != null) {
            throw new ValideringsfeilException(Collections.singleton(fnrfeil));
        }
    }

    @Override
    public MedicalRecordDTO createInTransfer(final String transferId, final PersondataDTO personalDataDTO) {
        // Validate personal data
        validateBaseData(personalDataDTO);

        // Convert personal data to medical record
        final Pasientjournal medicalRecord = MedicalRecordConverter.convertFromPersonalDataDTO(personalDataDTO); 

        // Save
        medicalRecordDAO.create(medicalRecord);
        final Avlevering transfer = transferDAO.fetchSingleInstance(transferId);
        transfer.getPasientjournal().add(medicalRecord);

        // Tracking.
        transfer.setOppdateringsinfo(createUpdateInfo());
        final MedicalRecordDTO medicalRecordDTO = MedicalRecordConverter.convertToMedicalRecordDTO(medicalRecord);
        final String transferIdForRecord = transferDAO.fetchTransferIdFromRecordId(medicalRecord.getUuid());
        medicalRecordDTO.setAvleveringsidentifikator(transferIdForRecord);

        // Update the users last used storage unit
        final Lagringsenhet lagringsenhet = medicalRecord.getLagringsenhet().get(0);
        final String username = sessionContext.getCallerPrincipal().getName();
        final String storageUnitId = lagringsenhet.getIdentifikator();
        if (storageUnitId != null && !storageUnitId.isEmpty()) {
            userDAO.updateLagringsenhet(username, storageUnitId);
        }

        return medicalRecordDTO;
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
        mappedQueries.entrySet().removeIf(entry ->
                                              entry.getValue() == null ||
                                              entry.getValue().isEmpty() ||
                                              SIZE.equals(entry.getKey()) ||
                                              PAGE.equals(entry.getKey()));
        return mappedQueries;
    }

    private void validateBaseData(final PersondataDTO persondataDTO) {
        // VALIDERING - Persondata
        ArrayList<Valideringsfeil> valideringsfeil = new Validator<>(PersondataDTO.class, persondataDTO).valider();

        //Validerer forholdet mellom dataoer
        DatoValiderer datoValiderer = new DatoValiderer();
        valideringsfeil.addAll(datoValiderer.valider(persondataDTO, konfigparam));

        Valideringsfeil fnrfeil = PersonnummerValiderer.valider(persondataDTO);
        if (fnrfeil != null) {
            if (!valideringsfeil.contains(fnrfeil)) {
                valideringsfeil.add(fnrfeil);
            }
        }

        Valideringsfeil fanearkidFeil = FanearkidValiderer.valider(persondataDTO, konfigparam);
        if (fanearkidFeil != null) {
            valideringsfeil.add(fanearkidFeil);
        }

        if (valideringsfeil.size() > 0) {
            throw new ValideringsfeilException(valideringsfeil);
        }
    }

    private void createAndAttachStorageUnit(List<Lagringsenhet> storageUnits) {
        // Create new storage units
        storageUnits.forEach(storageUnitDAO::create);
        // 
        List<Lagringsenhet> existingStorageUnits = storageUnits.stream()
                                                                .map(unit -> storageUnitDAO.fetchById(unit.getIdentifikator()))
                                                                .collect(Collectors.toList());
        storageUnits.clear();
        storageUnits.addAll(existingStorageUnits);
    }

    private Oppdateringsinfo createUpdateInfo() {
        Oppdateringsinfo oppdateringsinfo = new Oppdateringsinfo();
        oppdateringsinfo.setOppdatertAv(sessionContext.getCallerPrincipal().getName());
        oppdateringsinfo.setSistOppdatert(Calendar.getInstance());

        return oppdateringsinfo;
    }

}