package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StorageUnitAdapter extends XmlAdapter<String, StorageUnit> {
    
    @Override
    public StorageUnit unmarshal(final String storageUnit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String marshal(final StorageUnit storageUnit) {
        return "LID:" + storageUnit.getUuid() + ":" + storageUnit.getId();
    }
    
}