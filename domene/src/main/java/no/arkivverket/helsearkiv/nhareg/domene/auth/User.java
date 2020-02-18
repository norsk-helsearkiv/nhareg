package no.arkivverket.helsearkiv.nhareg.domene.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rollenavn")
    private Role role;

}