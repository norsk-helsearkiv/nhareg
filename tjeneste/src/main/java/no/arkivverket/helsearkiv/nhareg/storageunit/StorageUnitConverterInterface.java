package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.StorageUnitDTO;

import java.util.List;

public interface StorageUnitConverterInterface {
    
    StorageUnit toStorageUnit(StorageUnitDTO storageUnitDTO);
    
    StorageUnitDTO fromStorageUnit(StorageUnit storageUnit);
    
    List<StorageUnitDTO> fromStorageUnitList(List<StorageUnit> storageUnits);
}
