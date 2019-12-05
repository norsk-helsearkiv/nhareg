package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasientjournalSokeresultatDTO {
    
    private String uuid;
    
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

}
