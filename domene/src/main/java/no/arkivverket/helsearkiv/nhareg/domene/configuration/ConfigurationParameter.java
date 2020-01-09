package no.arkivverket.helsearkiv.nhareg.domene.configuration;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "konfigparam")
public class ConfigurationParameter {
    
    @Id
    private String navn;
    
    private String verdi;
    
    private String beskrivelse;

}