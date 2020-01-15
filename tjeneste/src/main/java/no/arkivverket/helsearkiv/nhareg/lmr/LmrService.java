package no.arkivverket.helsearkiv.nhareg.lmr;

import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.lmr.Lmr;
import no.arkivverket.helsearkiv.nhareg.domene.lmr.LmrDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

import javax.inject.Inject;
import java.util.Collections;

public class LmrService implements LmrServiceInterface {
    
    @Inject
    private LmrDAO lmrDAO;
    
    @Inject
    private ConfigurationDAO configurationDAO;
    
    @Inject
    private LmrConverterInterface lmrConverter;
    
    @Override
    public boolean isConfigured() {
        final String lmr = configurationDAO.getValue("lmr");
        return "true".equals(lmr);
    }

    @Override
    public LmrDTO getLmrInfo(final String pid) {
        final Lmr lmr = lmrDAO.fetchById(pid);
        
        if (lmr == null) {
            final ValidationError validationError = new ValidationError("fodselsnummer", "NationalIdentityDoesNotExist");
            throw new ValidationErrorException(Collections.singletonList(validationError));       
        }
        
        return lmrConverter.toLmrDTO(lmr);
    }
    
}