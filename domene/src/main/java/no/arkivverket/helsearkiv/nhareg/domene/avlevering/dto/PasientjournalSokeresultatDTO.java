package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

/**
 *
 * @author robing
 */
public class PasientjournalSokeresultatDTO {
    private String lagringsenhet;
    private String fodselsnummer;
    private String fanearkid;
    private String jnr;
    private String lnr;
    private String navn;
    private String faar;
    private String daar;
    private String oppdatertAv;
    private Long opprettetDato;
    private boolean avleveringLaast;
    private String avleveringsidentifikator;

    public String getFanearkid() {return this.fanearkid;}

    public void setFanearkid(String fanearkid){
        this.fanearkid = fanearkid;
    }

    public boolean isAvleveringLaast() {
        return avleveringLaast;
    }

    public void setAvleveringLaast(boolean avleveringLaast) {
        this.avleveringLaast = avleveringLaast;
    }

    private String uuid;

    public String getLagringsenhet() {
        return lagringsenhet;
    }

    public void setLagringsenhet(String lagringsenhet) {
        this.lagringsenhet = lagringsenhet;
    }

    public String getJnr() {
        return jnr;
    }

    public void setJnr(String jnr) {
        this.jnr = jnr;
    }

    public String getLnr() {
        return lnr;
    }

    public void setLnr(String lnr) {
        this.lnr = lnr;
    }

    public String getFaar() {
        return faar;
    }

    public void setFaar(String faar) {
        this.faar = faar;
    }

    public String getDaar() {
        return daar;
    }

    public void setDaar(String daar) {
        this.daar = daar;
    }

    public String getOppdatertAv() {
        return oppdatertAv;
    }

    public void setOppdatertAv(String oppdatertAv) {
        this.oppdatertAv = oppdatertAv;
    }

    public String getFodselsnummer() {
        return fodselsnummer;
    }

    public void setFodselsnummer(String fodselsnummer) {
        this.fodselsnummer = fodselsnummer;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getOpprettetDato() {
        return opprettetDato;
    }

    public void setOpprettetDato(Long opprettetDato) {
        this.opprettetDato = opprettetDato;
    }
    public String getAvleveringsidentifikator(){ return this.avleveringsidentifikator;}
    public void setAvleveringsidentifikator(String avleveringsidentifikator){ this.avleveringsidentifikator = avleveringsidentifikator;}

}
