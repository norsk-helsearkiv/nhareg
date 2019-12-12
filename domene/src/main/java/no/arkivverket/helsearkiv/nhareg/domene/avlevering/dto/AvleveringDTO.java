package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AvleveringDTO implements Serializable {
    
    protected String avleveringsidentifikator;

    protected String avleveringsbeskrivelse;

    protected Avtale avtale;

    protected String arkivskaper;

    protected String lagringsenhetformat;

    protected int antallPasientjournaler;

    private boolean laast;

    private boolean defaultAvlevering;

    public AvleveringDTO(Avlevering avlevering) {
        this.avleveringsidentifikator = avlevering.getAvleveringsidentifikator();
        this.avleveringsbeskrivelse = avlevering.getAvleveringsbeskrivelse();
        this.avtale = avlevering.getAvtale();
        this.arkivskaper = avlevering.getArkivskaper();
        this.antallPasientjournaler = avlevering.getPasientjournal().size();
        this.laast = avlevering.getLaast();
        this.lagringsenhetformat = avlevering.getLagringsenhetformat();
    }

    public Avlevering toAvlevering() {
        Avlevering a = new Avlevering();
        a.setLagringsenhetformat(getLagringsenhetformat());
        a.setArkivskaper(getArkivskaper());
        a.setAvleveringsbeskrivelse(getAvleveringsbeskrivelse());
        a.setAvleveringsidentifikator(getAvleveringsidentifikator());
        a.setAvtale(getAvtale());
        a.setLaast(isLaast());

        return a;
    }
}