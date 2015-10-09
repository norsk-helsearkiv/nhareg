package no.arkivverket.helsearkiv.nhareg.domene.konfig;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by haraldk on 09.10.15.
 * Klasse for konfigurerbare parametere som legges inn i databasen
 */
@Entity
public class Konfigparam {
    @Id
    private String navn;
    private String verdi;
    private String beskrivelse;

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getVerdi() {
        return verdi;
    }

    public void setVerdi(String verdi) {
        this.verdi = verdi;
    }
    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }


}
