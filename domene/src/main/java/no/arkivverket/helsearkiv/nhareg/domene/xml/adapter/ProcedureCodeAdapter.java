package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.ProcedureCode;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;

public class ProcedureCodeAdapter extends XmlAdapter<Set<ProcedureCode>, Set<ProcedureCode>> {

    @Override
    public Set<ProcedureCode> unmarshal(final Set<ProcedureCode> v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ProcedureCode> marshal(final Set<ProcedureCode> value) {
        if (value.isEmpty()) {
            value.add(new ProcedureCode());
        }
        
        return value;
    }
    
}