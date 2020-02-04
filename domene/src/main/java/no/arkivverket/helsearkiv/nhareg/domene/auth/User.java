package no.arkivverket.helsearkiv.nhareg.domene.auth;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bruker")
public class User {
    
    @Id
    @Column(name = "brukernavn")
    private String username;
    
    @Column(name = "passord")
    private String password;
    
    @Column(name = "authToken")
    private String authToken;
    
    @Column(name = "resetPassord")
    private String resetPassword;
    
    @Column(name = "defaultAvleveringsUuid")
    private String defaultTransferId;
    
    @Column(name = "printerzpl")
    private String printer;
  
    @Column(name = "lagringsenhet")
    private String storageUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rollenavn")
    private Role role;

}