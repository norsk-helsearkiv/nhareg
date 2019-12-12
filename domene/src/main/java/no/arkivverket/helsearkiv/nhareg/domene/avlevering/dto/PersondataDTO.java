package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersondataDTO implements Serializable {
    
    private String[] lagringsenheter;
    
    private String fanearkid;
    
    private String journalnummer;
    
    private String lopenummer;
    
    private String fodselsnummer;
    
    private String navn;
    
    private String kjonn;
    
    private String fodt;

    private String dod;

    private String fKontakt;

    private String sKontakt;
    
    private String uuid;

    private String merknad;

}