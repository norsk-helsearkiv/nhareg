package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Diagnosis;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiagnosisAdapter extends XmlAdapter<Set<Diagnosis>, Set<Diagnosis>> {
    
    @Override
    public Set<Diagnosis> unmarshal(final Set<Diagnosis> ignored) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Diagnosis> marshal(final Set<Diagnosis> value) {
        if (value.isEmpty()) {
            return Stream.of(new Diagnosis()).collect(Collectors.toSet());
        }
        
        return value;
    }
}
