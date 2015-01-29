package no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;

/**
 * Brukes for å kunne vise frem pasientjournaler med Avtale og Avlevering etter søk
 * @author robing
 */
public class PasientjournalAvlevering {
    
    private Avlevering avlevering;
    private Pasientjournal pasientjournal;

    public PasientjournalAvlevering(Avlevering avlevering, Pasientjournal pasientjournal) {
        setAvlevering(avlevering);
        setPasientjournal(pasientjournal);
    }
    
    public Avlevering getAvlevering() {
        return avlevering;
    }

    public void setAvlevering(Avlevering avlevering) {
        this.avlevering = avlevering;
    }

    public Pasientjournal getPasientjournal() {
        return pasientjournal;
    }

    public void setPasientjournal(Pasientjournal pasientjournal) {
        this.pasientjournal = pasientjournal;
    }   
}
