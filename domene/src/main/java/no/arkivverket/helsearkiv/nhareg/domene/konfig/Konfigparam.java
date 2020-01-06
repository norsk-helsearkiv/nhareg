package no.arkivverket.helsearkiv.nhareg.domene.konfig;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Konfigparam {
    
    @Id
    private String navn;
    
    private String verdi;
    
    private String beskrivelse;

}