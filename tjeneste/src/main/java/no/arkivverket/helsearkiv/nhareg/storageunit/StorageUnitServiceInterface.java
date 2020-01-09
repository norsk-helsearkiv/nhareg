package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.StorageUnitDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface StorageUnitServiceInterface {

    StorageUnit getById(final String id);

    StorageUnit create(final StorageUnit storageUnit);
 
    StorageUnit update(final StorageUnit storageUnit);

    List<RecordTransferDTO> getMedicalRecordsForId(final String id);

    List<StorageUnitDTO> getStorageUnits(final MultivaluedMap<String, String> queryParameters);

    void updateRecordStorageUnit(final List<String> medicalRecordIds, final StorageUnit storageUnit);

    void printMedicalRecord(final String id, final String username);
    
}