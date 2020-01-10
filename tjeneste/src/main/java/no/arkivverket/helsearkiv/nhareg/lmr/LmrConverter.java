package no.arkivverket.helsearkiv.nhareg.lmr;

import no.arkivverket.helsearkiv.nhareg.domene.lmr.Lmr;
import no.arkivverket.helsearkiv.nhareg.domene.lmr.LmrDTO;

public class LmrConverter implements LmrConverterInterface {
    
    @Override
    public LmrDTO toLmrDTO(final Lmr lmr) {
        if (lmr == null) {
            return null;
        }
        
        return new LmrDTO(lmr.getFnavn(), lmr.getEnavn(), lmr.getMnavn(), lmr.getDdato());
    }
    
}