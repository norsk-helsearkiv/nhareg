package no.arkivverket.helsearkiv.nhareg.domene.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrukerDTO {
    
    private String brukernavn;
    
    private RolleDTO rolle;
    
    private String password;
    
    private String passwordConfirm;
    
    private Boolean resetPassword;
    
    private String printerzpl;

    public BrukerDTO(final Bruker user) {
        this.brukernavn = user.getBrukernavn();
        this.printerzpl = user.getPrinterzpl();
        this.rolle = new RolleDTO(user.getRolle());
    }

    public BrukerDTO(final String brukernavn, final RolleDTO rolle, final String password,
                     final String passwordConfirm, final Boolean resetPassword) {
        this.brukernavn = brukernavn;
        this.rolle = rolle;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.resetPassword = resetPassword;
    }

    public Bruker toBruker(){
        Bruker bruker = new Bruker();
        bruker.setBrukernavn(brukernavn);
        bruker.setPassord(password);
        Rolle rolle = new Rolle();
        
        if (this.rolle != null) {
            rolle.setNavn(this.rolle.getNavn());
        }
        
        bruker.setRolle(rolle);
        bruker.setPrinterzpl(getPrinterzpl());
        
        return bruker;
    }
}