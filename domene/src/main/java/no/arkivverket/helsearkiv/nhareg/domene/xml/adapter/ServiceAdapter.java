package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.Service;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;

public class ServiceAdapter extends XmlAdapter<Set<Service>, Set<Service>> {
    
    @Override
    public Set<Service> unmarshal(final Set<Service> v) {
        throw new UnsupportedOperationException();
    }

    @Override 
    public Set<Service> marshal(final Set<Service> value) {
        if (value.isEmpty()) {
            value.add(new Service());
        }
        
        return value;
    }
}