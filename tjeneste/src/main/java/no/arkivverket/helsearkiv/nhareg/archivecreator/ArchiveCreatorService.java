package no.arkivverket.helsearkiv.nhareg.archivecreator;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveCreator;

import javax.inject.Inject;

public class ArchiveCreatorService implements ArchiveCreatorServiceInterface {
    
    @Inject
    private ArchiveCreatorDAO archiveCreatorDAO;

    @Override
    public ArchiveCreator getByName(final String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        
        return archiveCreatorDAO.fetchByName(name);
    }
    
}