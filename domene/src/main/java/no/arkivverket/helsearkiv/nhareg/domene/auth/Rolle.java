package no.arkivverket.helsearkiv.nhareg.domene.auth;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by haraldk on 15.04.15.
 */
@Entity
public class Rolle {

    @Id
    private String navn;

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }
}
