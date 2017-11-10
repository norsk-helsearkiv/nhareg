package no.arkivverket.helsearkiv.nhareg.domene.auth.dto;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;

/**
 * Created by haraldk on 08/11/2017.
 */
public class BrukerDTO {

    private String brukernavn;
    private RolleDTO rolle;
    private String password;
    private String passwordConfirm;
    private Boolean resetPassword;

    public BrukerDTO(final Bruker b){
        this.brukernavn = b.getBrukernavn();
        this.rolle = new RolleDTO(b.getRolle());
    }
    public BrukerDTO(){

    }
    public BrukerDTO(final String brukernavn, final RolleDTO rolle, final String password, final String passwordConfirm, final Boolean resetPassword){
        this.brukernavn = brukernavn;
        this.rolle = rolle;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.resetPassword = resetPassword;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public RolleDTO getRolle() {
        return rolle;
    }

    public void setRolle(RolleDTO rolle) {
        this.rolle = rolle;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(Boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public Bruker toBruker(){
        Bruker b = new Bruker();
        b.setBrukernavn(brukernavn);
        b.setPassord(password);
        Rolle r = new Rolle();
        if (rolle!=null) {
            r.setNavn(rolle.getNavn());
        }
        b.setRolle(r);
        return b;
    }
}
