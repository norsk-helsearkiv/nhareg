package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.ReferencePeriod;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;

public class ReferencePeriodAdapter extends XmlAdapter<Set<ReferencePeriod>, Set<ReferencePeriod>> {

    @Override
    public Set<ReferencePeriod> unmarshal(final Set<ReferencePeriod> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ReferencePeriod> marshal(final Set<ReferencePeriod> value) {
        if (value.isEmpty()) {
            value.add(new ReferencePeriod());
        }
        
        return value;
    }
    
}