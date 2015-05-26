package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import java.io.Serializable;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;

/**
 *
 * @author robing
 */
public class AvleveringDTO implements Serializable{
    
    protected String avleveringsidentifikator;
    protected String avleveringsbeskrivelse;
    protected Avtale avtale;
    protected String arkivskaper;
    protected int antallPasientjournaler;
    private  boolean laast;

    public AvleveringDTO() {
    }
    
    public AvleveringDTO(Avlevering avlevering) {
        this.avleveringsidentifikator = avlevering.getAvleveringsidentifikator();
        this.avleveringsbeskrivelse = avlevering.getAvleveringsbeskrivelse();
        this.avtale = avlevering.getAvtale();
        this.arkivskaper = avlevering.getArkivskaper();
        this.antallPasientjournaler = avlevering.getPasientjournal().size();
        this.laast = avlevering.isLaast();
    }

    public void setAntallPasientjournaler(int antallPasientjournaler) {
        this.antallPasientjournaler = antallPasientjournaler;
    }

    public int getAntallPasientjournaler() {
        return antallPasientjournaler;
    }
    
    public String getAvleveringsidentifikator() {
        return avleveringsidentifikator;
    }

    public void setAvleveringsidentifikator(String avleveringsidentifikator) {
        this.avleveringsidentifikator = avleveringsidentifikator;
    }

    public String getAvleveringsbeskrivelse() {
        return avleveringsbeskrivelse;
    }

    public void setAvleveringsbeskrivelse(String avleveringsbeskrivelse) {
        this.avleveringsbeskrivelse = avleveringsbeskrivelse;
    }

    public Avtale getAvtale() {
        return avtale;
    }

    public void setAvtale(Avtale avtale) {
        this.avtale = avtale;
    }

    public String getArkivskaper() {
        return arkivskaper;
    }

    public void setArkivskaper(String arkivskaper) {
        this.arkivskaper = arkivskaper;
    }

    public void setLaast(boolean laast){
        this.laast = laast;
    }
    public boolean isLaast (){
        return this.laast;
    }
    
}
