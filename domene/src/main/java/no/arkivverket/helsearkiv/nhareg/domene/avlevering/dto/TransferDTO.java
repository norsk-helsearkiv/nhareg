package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TransferDTO implements Serializable {
    
    protected String avleveringsidentifikator;

    protected String avleveringsbeskrivelse;

    protected Agreement avtale;

    protected String arkivskaper;

    protected String lagringsenhetformat;

    protected int antallPasientjournaler;

    private boolean laast;

    private boolean defaultAvlevering;

    public TransferDTO(final Avlevering transfer) {
        this.avleveringsidentifikator = transfer.getAvleveringsidentifikator();
        this.avleveringsbeskrivelse = transfer.getAvleveringsbeskrivelse();
        this.avtale = transfer.getAgreement();
        this.arkivskaper = transfer.getArkivskaper();
        this.antallPasientjournaler = transfer.getPasientjournal().size();
        this.laast = transfer.isLaast();
        this.lagringsenhetformat = transfer.getLagringsenhetformat();
    }

    public Avlevering toTransfer() {
        final Avlevering transfer = new Avlevering();
        transfer.setLagringsenhetformat(getLagringsenhetformat());
        transfer.setArkivskaper(getArkivskaper());
        transfer.setAvleveringsbeskrivelse(getAvleveringsbeskrivelse());
        transfer.setAvleveringsidentifikator(getAvleveringsidentifikator());
        transfer.setAgreement(getAvtale());
        transfer.setLaast(isLaast());

        return transfer;
    }
    
}