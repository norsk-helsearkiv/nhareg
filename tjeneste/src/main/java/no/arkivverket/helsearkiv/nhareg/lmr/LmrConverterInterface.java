package no.arkivverket.helsearkiv.nhareg.lmr;

import no.arkivverket.helsearkiv.nhareg.domene.lmr.Lmr;
import no.arkivverket.helsearkiv.nhareg.domene.lmr.LmrDTO;

public interface LmrConverterInterface {
    
    LmrDTO toLmrDTO(Lmr lmr);
    
}
