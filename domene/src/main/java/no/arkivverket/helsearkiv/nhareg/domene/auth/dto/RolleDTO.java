package no.arkivverket.helsearkiv.nhareg.domene.auth.dto;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Role;

/**
 * Created by haraldk on 08/11/2017.
 */
public class RolleDTO {
    private String navn;

    public RolleDTO() {
    }
    
    public RolleDTO(final Role role) {
        this.navn = role.getName();
    }
    
    public RolleDTO(final String navn) {
        this.navn = navn;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }
}
