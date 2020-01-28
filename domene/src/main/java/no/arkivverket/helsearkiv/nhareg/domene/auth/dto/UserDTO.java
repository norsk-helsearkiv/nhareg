package no.arkivverket.helsearkiv.nhareg.domene.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @JsonProperty(value = "brukernavn")
    private String username;

    @JsonProperty(value = "rolle")
    private RoleDTO role;
    
    private String password;
    
    private String passwordConfirm;
    
    private Boolean resetPassword;
    
    private String printerzpl;

}