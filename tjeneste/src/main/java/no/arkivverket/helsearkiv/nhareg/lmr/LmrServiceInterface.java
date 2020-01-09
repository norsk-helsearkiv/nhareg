package no.arkivverket.helsearkiv.nhareg.lmr;

import no.arkivverket.helsearkiv.nhareg.domene.lmr.LmrDTO;

public interface LmrServiceInterface {
    
    boolean isConfigured();
    
    LmrDTO getLmrInfo(final String pid);
    
}