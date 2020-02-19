package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.StorageUnitDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface StorageUnitServiceInterface {

    StorageUnitDTO create(final StorageUnit storageUnit);

    StorageUnitDTO update(final StorageUnit storageUnit);
    
    StorageUnitDTO getById(final String id);

    StorageUnitDTO getByIdentifier(final String id);
    
    List<RecordTransferDTO> getMedicalRecordsForId(final String id);

    List<StorageUnitDTO> getStorageUnits(final MultivaluedMap<String, String> queryParameters);

    void updateRecordStorageUnit(final List<String> medicalRecordIds, final StorageUnitDTO storageUnitDTO);

    void printMedicalRecord(final String id, final String username);
    
}