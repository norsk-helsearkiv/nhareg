package no.arkivverket.helsearkiv.nhareg.domene.auth;

import javax.persistence.*;

/**
 * Created by haraldk on 15.04.15.
 */
@Entity
public class Bruker {
    @Id
    private String brukernavn;
    private String passord;
    private String authToken;
    private String resetPassord;
    private String defaultAvleveringsUuid;
    private String printerzpl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rollenavn")
    private Rolle rolle;

    public String getDefaultAvleveringsUuid() {
        return defaultAvleveringsUuid;
    }

    public void setDefaultAvleveringsUuid(String defaultAvleveringsUuid) {
        this.defaultAvleveringsUuid = defaultAvleveringsUuid;
    }

    public String getLagringsenhet() {
        return lagringsenhet;
    }

    public void setLagringsenhet(String lagringsenhet) {
        this.lagringsenhet = lagringsenhet;
    }

    private String lagringsenhet;

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }

    public Rolle getRolle() {
        return rolle;
    }

    public void setRolle(Rolle rolle) {
        this.rolle = rolle;
    }
    
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    
    public String getResetPassord() {
        return resetPassord;
    }

    public void setResetPassord(String resetPassord) {
        this.resetPassord = resetPassord;
    }

    public String getPrinterzpl() {
        return printerzpl;
    }

    public void setPrinterzpl(String printerzpl) {
        this.printerzpl = printerzpl;
    }
}
