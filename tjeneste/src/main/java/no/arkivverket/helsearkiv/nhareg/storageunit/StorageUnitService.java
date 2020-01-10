package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.StorageUnitDTO;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordConverterInterface;
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
import java.util.stream.Collectors;

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
    
    @Inject
    private MedicalRecordConverterInterface medicalRecordConverter;
    
    @Inject
    private StorageUnitConverterInterface storageUnitConverter;

    @Override
    public StorageUnitDTO getById(final String id) {
        final StorageUnit storageUnit = storageUnitDAO.fetchById(id);
        
        if (storageUnit == null) {
            return null;
        }
        
        return storageUnitConverter.fromStorageUnit(storageUnit);
    }

    @Override
    public StorageUnitDTO create(final StorageUnit storageUnit) {
        final StorageUnit newStorageUnit = storageUnitDAO.create(storageUnit);
        
        return storageUnitConverter.fromStorageUnit(newStorageUnit);
    }

    @Override
    public StorageUnitDTO update(final StorageUnit storageUnit) {
        final StorageUnit updated = storageUnitDAO.update(storageUnit);
        
        return storageUnitConverter.fromStorageUnit(updated);
    }

    @Override
    public List<RecordTransferDTO> getMedicalRecordsForId(final String id) {
        final List<MedicalRecord> medicalRecords = storageUnitDAO.fetchMedicalRecordsForStorageUnit(id);
        
        return medicalRecordConverter.toRecordTransferDTOList(medicalRecords);
    }

    @Override
    public List<StorageUnitDTO> getStorageUnits(final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedParameters = ParameterConverter.multivaluedToMap(queryParameters);
        final List<StorageUnit> storageUnits = storageUnitDAO.fetchAll(mappedParameters);
        
        return storageUnits.stream().map(storageUnitConverter::fromStorageUnit).collect(Collectors.toList());
    }

    @Override
    public void updateRecordStorageUnit(final List<String> medicalRecordIds, final StorageUnitDTO storageUnitDTO) {
        for (final String recordId: medicalRecordIds) {
            final MedicalRecord medicalRecord = medicalRecordDAO.fetchById(recordId);
            medicalRecord.getStorageUnit().clear();
            final StorageUnit storageUnit = storageUnitConverter.toStorageUnit(storageUnitDTO);
            medicalRecord.getStorageUnit().add(storageUnit);
        }
    }

    @Override
    public void printMedicalRecord(final String id, final String username) {
        final StorageUnit storageUnit = storageUnitDAO.fetchById(id);
        final Integer medicalRecordCount = storageUnitDAO.fetchCountOfRecordsForStorageUnit(id);
        final String firstTransferId = transferDAO.fetchFirstTransferIdFromStorageUnit(storageUnit.getUuid());
        final Transfer transfer = transferDAO.fetchById(firstTransferId);
        final User user = userDAO.fetchByUsername(username);

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
        
        storageUnit.setPrint(true);

        storageUnitDAO.update(storageUnit);
    }

}