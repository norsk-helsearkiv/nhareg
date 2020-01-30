package no.arkivverket.helsearkiv.nhareg.domene.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveAuthor;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ArchiveAuthorAdapter extends XmlAdapter<String, ArchiveAuthor> {
    
    @Override
    public ArchiveAuthor unmarshal(final String archiveCreator) {
        return null;
    }

    @Override 
    public String marshal(final ArchiveAuthor archiveAuthor) {
        if (archiveAuthor == null) {
            return "";
        }
        
        return archiveAuthor.getName();
    }
    
}