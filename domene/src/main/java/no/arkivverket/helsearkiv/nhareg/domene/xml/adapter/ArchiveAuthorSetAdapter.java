package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveAuthor;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class ArchiveAuthorSetAdapter extends XmlAdapter<Set<String>, Set<ArchiveAuthor>> {

    @Override
    public Set<ArchiveAuthor> unmarshal(final Set<String> ignored) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> marshal(final Set<ArchiveAuthor> value) {
        if (value.isEmpty()) {
            return Collections.singleton("");
        }
        
        return value.stream().map(ArchiveAuthor::getName).collect(Collectors.toSet());
    }
    
}