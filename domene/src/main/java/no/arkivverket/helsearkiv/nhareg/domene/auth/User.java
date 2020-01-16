package no.arkivverket.helsearkiv.nhareg.domene.auth;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bruker")
public class User {
    
    @Id
    private String brukernavn;
    
    private String passord;
    
    private String authToken;
    
    private String resetPassord;
    
    private String defaultAvleveringsUuid;
    
    private String printerzpl;
  
    private String lagringsenhet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rollenavn")
    private Role role;

}
