package no.arkivverket.helsearkiv.nhareg.domene.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @XmlElement(name = "brukernavn")
    private String username;

    @XmlElement(name = "rolle")
    private RoleDTO role;
    
    private String password;
    
    private String passwordConfirm;
    
    private Boolean resetPassword;
    
    private String printerzpl;

}