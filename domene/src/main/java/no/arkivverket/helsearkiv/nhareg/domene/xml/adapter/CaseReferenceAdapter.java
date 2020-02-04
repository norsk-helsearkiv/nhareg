package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.CaseReference;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;

public class CaseReferenceAdapter extends XmlAdapter<Set<CaseReference>, Set<CaseReference>> {

    @Override
    public Set<CaseReference> unmarshal(final Set<CaseReference> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<CaseReference> marshal(final Set<CaseReference> value) {
        if (value.isEmpty()) {
            value.add(new CaseReference());
        }
        
        return value;
    }
    
}