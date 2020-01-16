package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.StorageUnitDTO;

public interface StorageUnitConverterInterface {
    
    StorageUnit toStorageUnit(StorageUnitDTO storageUnitDTO);
    
    StorageUnitDTO fromStorageUnit(StorageUnit storageUnit);
    
}
