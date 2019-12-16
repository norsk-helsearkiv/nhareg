package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.RecordTransferDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface StorageUnitServiceInterface {

    Lagringsenhet getById(final String id);

    Lagringsenhet create(final Lagringsenhet storageUnit);
 
    Lagringsenhet update(final Lagringsenhet storageUnit);

    List<RecordTransferDTO> getMedicalRecordsForId(final String id);

    Integer getCountOfRecordsForStorageUnit(final String storageUnitId);

    String getRecordIdFromStorageUnit(final String storageUnitId);

    List<Lagringsenhet> getStorageUnits(final MultivaluedMap<String, String> queryParameters);

    void updateRecordStorageUnit(final List<String> medicalRecordIds, final Lagringsenhet storageUnit);

    void printMedicalRecord(final String id);
    
}