package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TransferDTO implements Serializable {
    
    protected String avleveringsidentifikator;

    protected String avleveringsbeskrivelse;

    protected Avtale avtale;

    protected String arkivskaper;

    protected String lagringsenhetformat;

    protected int antallPasientjournaler;

    private boolean laast;

    private boolean defaultAvlevering;

    public TransferDTO(final Avlevering transfer) {
        this.avleveringsidentifikator = transfer.getAvleveringsidentifikator();
        this.avleveringsbeskrivelse = transfer.getAvleveringsbeskrivelse();
        this.avtale = transfer.getAvtale();
        this.arkivskaper = transfer.getArkivskaper();
        this.antallPasientjournaler = transfer.getPasientjournal().size();
        this.laast = transfer.getLaast();
        this.lagringsenhetformat = transfer.getLagringsenhetformat();
    }

    public Avlevering toTransfer() {
        final Avlevering transfer = new Avlevering();
        transfer.setLagringsenhetformat(getLagringsenhetformat());
        transfer.setArkivskaper(getArkivskaper());
        transfer.setAvleveringsbeskrivelse(getAvleveringsbeskrivelse());
        transfer.setAvleveringsidentifikator(getAvleveringsidentifikator());
        transfer.setAvtale(getAvtale());
        transfer.setLaast(isLaast());

        return transfer;
    }
    
}