package no.arkivverket.helsearkiv.nhareg.domene.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StorageUnitAdapter extends XmlAdapter<String, StorageUnit> {
    
    @Override
    public StorageUnit unmarshal(final String storageUnit) {
        final String[] storageUnitId = storageUnit.split(";");
        
        return new StorageUnit(storageUnitId[0], storageUnitId[1], false);
    }

    @Override
    public String marshal(final StorageUnit storageUnit) {
        return storageUnit.getUuid() + ":" + storageUnit.getId();
    }
    
}