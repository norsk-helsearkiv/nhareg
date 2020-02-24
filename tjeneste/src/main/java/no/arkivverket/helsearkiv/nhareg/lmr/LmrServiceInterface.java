package no.arkivverket.helsearkiv.nhareg.lmr;

import no.arkivverket.helsearkiv.nhareg.domene.lmr.LmrDTO;

public interface LmrServiceInterface {
    
    LmrDTO getLmrInfo(final String pid);
    
}