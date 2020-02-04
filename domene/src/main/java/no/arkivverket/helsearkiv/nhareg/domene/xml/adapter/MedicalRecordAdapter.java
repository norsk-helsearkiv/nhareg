package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;

public class MedicalRecordAdapter extends XmlAdapter<Set<MedicalRecord>, Set<MedicalRecord>> {

    @Override
    public Set<MedicalRecord> unmarshal(final Set<MedicalRecord> v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<MedicalRecord> marshal(final Set<MedicalRecord> value) {
        if (value.isEmpty()) {
            value.add(new MedicalRecord());
        }
        
        return value;
    }
}