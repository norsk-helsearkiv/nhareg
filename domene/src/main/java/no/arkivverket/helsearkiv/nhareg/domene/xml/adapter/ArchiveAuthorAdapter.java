package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveAuthor;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArchiveAuthorAdapter extends XmlAdapter<Set<ArchiveAuthor>, Set<ArchiveAuthor>> {

    @Override
    public Set<ArchiveAuthor> unmarshal(final Set<ArchiveAuthor> ignored) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ArchiveAuthor> marshal(final Set<ArchiveAuthor> value) {
        if (value.isEmpty()) {
            final ArchiveAuthor author = new ArchiveAuthor();

            return Stream.of(author).collect(Collectors.toSet());
        }
        
        return value;
    }
}
