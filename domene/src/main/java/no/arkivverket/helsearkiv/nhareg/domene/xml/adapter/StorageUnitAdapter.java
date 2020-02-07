package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StorageUnitAdapter extends XmlAdapter<Set<String>, Set<StorageUnit>> {
    
    @Override
    public Set<StorageUnit> unmarshal(final Set<String> storageUnits) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> marshal(final Set<StorageUnit> storageUnits) {
        if (storageUnits.isEmpty()) {
            return Stream.of("").collect(Collectors.toSet());
        }
        
        return storageUnits.stream()
                           .map(storageUnit -> "LID:" + storageUnit.getUuid() + ":" + storageUnit.getId())
                           .collect(Collectors.toSet());
    }
    
}