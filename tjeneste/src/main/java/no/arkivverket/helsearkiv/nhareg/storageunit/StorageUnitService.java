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
import no.arkivverket.helsearkiv.nhareg.util.ParameterConverter;
import no.arkivverket.helsearkiv.nhareg.util.SocketPrinter;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StorageUnitService implements StorageUnitServiceInterface {

    @Inject
    private StorageUnitDAO storageUnitDAO;
    
    @Inject
    private TransferDAO transferDAO;
    
    @Inject
    private UserDAO userDAO;
    
    @Inject
    private MedicalRecordDAO medicalRecordDAO;

    @Inject
    private ConfigurationDAO configurationDAO;

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
    public List<Lagringsenhet> getStorageUnits(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedParameters = ParameterConverter.multivaluedToMap(queryParameters);
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
    public void printMedicalRecord(final String id, final String username) {
        final Lagringsenhet storageUnit = storageUnitDAO.fetchById(id);
        final Integer medicalRecordCount = storageUnitDAO.fetchCountOfRecordsForStorageUnit(id);
        final String firstTransferId = transferDAO.fetchFirstTransferIdFromStorageUnit(storageUnit.getUuid());
        final Avlevering transfer = transferDAO.fetchById(firstTransferId);
        final Bruker user = userDAO.fetchByUsername(username);

        String printerIp = user.getPrinterzpl();
        if (printerIp == null) {
            printerIp = "127.0.0.1";
        }

        Integer printerPort = configurationDAO.getInt(ConfigurationDAO.KONFIG_PRINTER_PORT);
        if (printerPort == null) {
            printerPort = 9100;
        }

        final String fileTemplatePath = configurationDAO.getValue(ConfigurationDAO.KONFIG_TEMPLATEFILE);
        try {
            final String toPrint = new EtikettBuilder().buildContent(fileTemplatePath, storageUnit, transfer,
                                                            medicalRecordCount);

            new SocketPrinter().print(toPrint, printerIp, printerPort);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        
        storageUnit.setUtskrift(true);

        storageUnitDAO.update(storageUnit);
    }

}