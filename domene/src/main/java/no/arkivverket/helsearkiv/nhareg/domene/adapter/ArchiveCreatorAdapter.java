package no.arkivverket.helsearkiv.nhareg.domene.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveCreator;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ArchiveCreatorAdapter extends XmlAdapter<String, ArchiveCreator> {
    
    @Override
    public ArchiveCreator unmarshal(final String archiveCreator) {
        return null;
    }

    @Override 
    public String marshal(final ArchiveCreator archiveCreator) {
        if (archiveCreator == null) {
            return "";
        }
        
        return archiveCreator.getName();
    }
    
}