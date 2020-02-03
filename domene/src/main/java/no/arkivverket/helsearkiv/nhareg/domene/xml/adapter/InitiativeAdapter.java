package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.Initiative;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;

public class InitiativeAdapter extends XmlAdapter<Set<Initiative>, Set<Initiative>> {

    @Override
    public Set<Initiative> unmarshal(final Set<Initiative> v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Initiative> marshal(final Set<Initiative> value) {
        if (value.isEmpty()) {
            value.add(new Initiative());
        }
        
        return value;
    }
}