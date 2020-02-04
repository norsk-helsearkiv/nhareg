package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveAuthor;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ArchiveAuthorAdapter extends XmlAdapter<String, ArchiveAuthor> {
    
    @Override
    public ArchiveAuthor unmarshal(final String archiveCreator) {
        throw new UnsupportedOperationException();
    }

    @Override 
    public String marshal(final ArchiveAuthor archiveAuthor) {
        return archiveAuthor.getName();
    }
    
}