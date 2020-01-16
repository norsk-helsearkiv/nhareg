package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.StorageUnitDTO;

public class StorageUnitConverter implements StorageUnitConverterInterface {
    
    @Override
    public StorageUnit toStorageUnit(final StorageUnitDTO storageUnitDTO) {
        if (storageUnitDTO == null) {
            return null;
        }
        
        return new StorageUnit(storageUnitDTO.getId(), storageUnitDTO.getUuid(), storageUnitDTO.isPrint());
    }

    @Override
    public StorageUnitDTO fromStorageUnit(final StorageUnit storageUnit) {
        if (storageUnit == null) {
            return null;
        }
        
        return new StorageUnitDTO(storageUnit.getId(), storageUnit.getUuid(), storageUnit.isPrint());
    }
}
