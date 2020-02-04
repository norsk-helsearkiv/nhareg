package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Diagnosis;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;

public class DiagnosisAdapter extends XmlAdapter<Set<Diagnosis>, Set<Diagnosis>> {

    @Override
    public Set<Diagnosis> unmarshal(final Set<Diagnosis> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Diagnosis> marshal(final Set<Diagnosis> value) {
        value.forEach(diagnosis -> {
            final String codingSystem = diagnosis.getDiagnosisCodingSystem();
            if (codingSystem == null || codingSystem.isEmpty()) {
                diagnosis.setDiagnosisCodingSystem("0");
            }
        });
        
        return value;
    }
}