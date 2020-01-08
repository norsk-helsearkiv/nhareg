package no.arkivverket.helsearkiv.nhareg.domene.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Role;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;

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

    public BrukerDTO(final User user) {
        this.brukernavn = user.getBrukernavn();
        this.printerzpl = user.getPrinterzpl();
        this.rolle = new RolleDTO(user.getRole());
    }

    public BrukerDTO(final String brukernavn, final RolleDTO rolle, final String password,
                     final String passwordConfirm, final Boolean resetPassword) {
        this.brukernavn = brukernavn;
        this.rolle = rolle;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.resetPassword = resetPassword;
    }

    public User toBruker() {
        final User user = new User();
        final Role role = new Role();
        
        user.setBrukernavn(brukernavn);
        user.setPassord(password);
        
        if (this.rolle != null) {
            role.setName(this.rolle.getNavn());
        }
        
        user.setRole(role);
        user.setPrinterzpl(getPrinterzpl());
        
        return user;
    }
    
}