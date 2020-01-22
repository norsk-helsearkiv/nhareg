package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Role;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.RoleDTO;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.UserDTO;

public class UserConverter implements UserConverterInterface {
    
    @Override
    public UserDTO fromUser(final User user) {
        final RoleDTO roleDTO = new RoleDTO();
        final Role role = user.getRole();
        
        if (role != null) {
            roleDTO.setName(role.getName());
        }
        
        return new UserDTO(user.getUsername(), roleDTO, user.getPassword(),
                           user.getPassword(), false, user.getPrinter());
    }

    @Override
    public User toUser(final UserDTO userDTO) {
        final User user = new User();
        final Role role = new Role();

        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        if (userDTO.getRole() != null) {
            role.setName(userDTO.getRole().getName());
        }

        user.setRole(role);
        user.setPrinter(userDTO.getPrinterzpl());

        return user;
    }
    
}