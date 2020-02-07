package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MedicalRecordAdapter extends XmlAdapter<Set<MedicalRecord>, Set<MedicalRecord>> {

    @Override
    public Set<MedicalRecord> unmarshal(final Set<MedicalRecord> ignored) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<MedicalRecord> marshal(final Set<MedicalRecord> value) {
        // Filter out deleted records
        final Set<MedicalRecord> filtered = value.stream()
                                                 .filter(record -> record.getDeleted() == null ||
                                                     (record.getDeleted() != null && !record.getDeleted()))
                                                 .collect(Collectors.toSet());
        
        if (filtered.isEmpty()) {
            filtered.add(MedicalRecord.builder()
                                      .archiveAuthors(new HashSet<>())
                                      .diagnosis(new HashSet<>())
                                      .storageUnits(new HashSet<>())
                                      .build());
        }
        
        return filtered;
    }
    
}