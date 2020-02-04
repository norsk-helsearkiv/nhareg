package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.HospitalAdmission;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;

public class HospitalAdmissionAdapter extends XmlAdapter<Set<HospitalAdmission>, Set<HospitalAdmission>> {
    
    @Override
    public Set<HospitalAdmission> unmarshal(final Set<HospitalAdmission> v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<HospitalAdmission> marshal(final Set<HospitalAdmission> value) {
        if (value.isEmpty()) {
            value.add(new HospitalAdmission());
        }
        
        return value;
    }
}