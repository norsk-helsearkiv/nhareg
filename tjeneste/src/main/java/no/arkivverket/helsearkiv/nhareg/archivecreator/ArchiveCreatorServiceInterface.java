package no.arkivverket.helsearkiv.nhareg.archivecreator;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveCreator;

public interface ArchiveCreatorServiceInterface {
    
    ArchiveCreator getByName(final String name);
    
}
