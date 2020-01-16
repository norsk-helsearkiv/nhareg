package no.arkivverket.helsearkiv.nhareg.lmr;

import no.arkivverket.helsearkiv.nhareg.domene.lmr.Lmr;
import no.arkivverket.helsearkiv.nhareg.domene.lmr.LmrDTO;

import java.time.format.DateTimeFormatter;

public class LmrConverter implements LmrConverterInterface {
    
    @Override
    public LmrDTO toLmrDTO(final Lmr lmr) {
        if (lmr == null) {
            return null;
        }
        final String deathDate =  lmr.getDdato().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        
        return new LmrDTO(lmr.getFnavn(), lmr.getEnavn(), lmr.getMnavn(), deathDate);
    }
    
}