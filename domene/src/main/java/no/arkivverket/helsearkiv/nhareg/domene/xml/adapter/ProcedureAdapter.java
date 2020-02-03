package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.Procedure;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;

public class ProcedureAdapter extends XmlAdapter<Set<Procedure>, Set<Procedure>> {

    @Override 
    public Set<Procedure> unmarshal(final Set<Procedure> v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Procedure> marshal(final Set<Procedure> value) {
        if (value.isEmpty()) {
            value.add(new Procedure());
        }
        
        return value;
    }
    
}