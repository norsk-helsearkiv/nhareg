package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.UserDTO;

public interface UserConverterInterface {
    
    UserDTO fromUser(final User user);
    
    User toUser(final UserDTO userDTO); 
    
}