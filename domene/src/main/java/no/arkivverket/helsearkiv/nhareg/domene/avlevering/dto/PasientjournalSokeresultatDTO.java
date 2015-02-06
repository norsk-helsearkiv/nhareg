package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

/**
 *
 * @author robing
 */
public class PasientjournalSokeresultatDTO {

    private String avtale;
    private String avlevering;
    private String fodselsnummer;
    private String navn;
    private String uuid;

    public String getAvtale() {
        return avtale;
    }

    public void setAvtale(String avtale) {
        this.avtale = avtale;
    }

    public String getAvlevering() {
        return avlevering;
    }

    public void setAvlevering(String avlevering) {
        this.avlevering = avlevering;
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
    
    
}
