package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.UserDTO;

import java.util.List;

public interface UserConverterInterface {
    
    UserDTO fromUser(final User user);
    
    List<UserDTO> fromUserList(final List<User> users); 
    
    User toUser(final UserDTO userDTO); 
    
}