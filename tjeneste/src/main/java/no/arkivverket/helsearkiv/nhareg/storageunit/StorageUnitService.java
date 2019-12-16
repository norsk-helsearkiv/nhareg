package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordConverter;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordDAO;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferDAO;
import no.arkivverket.helsearkiv.nhareg.user.UserDAO;
import no.arkivverket.helsearkiv.nhareg.util.EtikettBuilder;
import no.arkivverket.helsearkiv.nhareg.util.SocketPrinter;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageUnitService implements StorageUnitServiceInterface {

    @Resource
    private SessionContext sessionContext;

    @Inject
    private StorageUnitDAO storageUnitDAO;
    
    @Inject
    private TransferDAO transferDAO;
    
    @Inject
    private UserDAO userDAO;
    
    @Inject
    private MedicalRecordDAO medicalRecordDAO;

    private static final String NOT_UNIQUE_CONSTRAINT = "NotUnique";
    private static final String STORAGE_UNIT_ATTRIBUTE = "lagringsenheter";

    @Override
    public Lagringsenhet getById(String id) {
        return storageUnitDAO.fetchById(id);
    }

    @Override
    public Lagringsenhet create(final Lagringsenhet storageUnit) {
        return storageUnitDAO.create(storageUnit);
    }

    @Override
    public Lagringsenhet update(final Lagringsenhet storageUnit) {
        return storageUnitDAO.update(storageUnit);
    }

    @Override
    public List<RecordTransferDTO> getMedicalRecordsForId(final String id) {
        List<Pasientjournal> medicalRecords = storageUnitDAO.fetchMedicalRecordsForStorageUnit(id);
        return MedicalRecordConverter.convertToRecordTransferDTOList(medicalRecords);
    }

    @Override
    public Integer getCountOfRecordsForStorageUnit(final String storageUnitId) {
        return storageUnitDAO.fetchCountOfRecordsForStorageUnit(storageUnitId);
    }

    @Override 
    public String getRecordIdFromStorageUnit(final String storageUnitId) {
        return storageUnitDAO.fetchRecordIdFromStorageUnit(storageUnitId);
    }

    @Override
    public List<Lagringsenhet> getStorageUnits(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedParameters = convertToMap(queryParameters);
        return storageUnitDAO.fetchAll(mappedParameters);
    }

    @Override
    public void updateRecordStorageUnit(final List<String> medicalRecordIds, final Lagringsenhet storageUnit) {
        for (final String recordId: medicalRecordIds) {
            final Pasientjournal medicalRecord = medicalRecordDAO.fetchById(recordId);
            medicalRecord.getLagringsenhet().clear();
            medicalRecord.getLagringsenhet().add(storageUnit);
        }
    }

    @Override
    public void printMedicalRecord(final String id) {
        final Lagringsenhet storageUnit = storageUnitDAO.fetchById(id);
        final Integer medicalRecordCount = storageUnitDAO.fetchCountOfRecordsForStorageUnit(id);
        final String firstTransferId = transferDAO.fetchFirstTransferIdFromStorageUnit(storageUnit.getUuid());
        final Avlevering transfer = transferDAO.fetchById(firstTransferId);
        final String username = sessionContext.getCallerPrincipal().getName();;
        final Bruker user = userDAO.fetchByUsername(username);

        String printerIp = user.getPrinterzpl();
        if (printerIp == null) {
            printerIp = "127.0.0.1";
        }

        Integer printerPort = konfigParam.getInt(ConfigurationDAO.KONFIG_PRINTER_PORT);
        if (printerPort == null) {
            printerPort = 9100;
        }

        String fileTemplatePath = konfigParam.getVerdi(ConfigurationDAO.KONFIG_TEMPLATEFILE);
        String toPrint = new EtikettBuilder().buildContent(fileTemplatePath, storageUnit, transfer, medicalRecordCount);

        new SocketPrinter().print(toPrint, printerIp, printerPort);
        storageUnit.setUtskrift(true);

        getEntityManager().merge(storageUnit);
        return Response.ok().build();

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
