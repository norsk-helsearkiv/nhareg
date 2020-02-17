package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Role;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.RoleDTO;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter implements UserConverterInterface {
    
    @Override
    public UserDTO fromUser(final User user) {
        if (user == null) {
            return null;
        }
        
        final RoleDTO roleDTO = new RoleDTO();
        final Role role = user.getRole();
        final boolean resetPassword = "Y".equals(user.getResetPassword());
        
        if (role != null) {
            roleDTO.setName(role.getName());
        }
        
        return UserDTO.builder()
                      .username(user.getUsername())
                      .role(roleDTO)
                      .password(user.getPassword())
                      .passwordConfirm(user.getPassword())
                      .resetPassword(resetPassword)
                      .printer(user.getPrinter())
                      .build();
    }

    @Override
    public List<UserDTO> fromUserList(final List<User> users) {
        return users.stream()
                    .map(this::fromUser)
                    .collect(Collectors.toList());        
    }

    @Override
    public User toUser(final UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        
        final Role role = new Role();
        final Boolean resetPassword = userDTO.getResetPassword();
        final String triggerReset = resetPassword != null && resetPassword ? "Y" : "";
        
        if (userDTO.getRole() != null) {
            role.setName(userDTO.getRole().getName());
        }        
        
        return User.builder()
                   .username(userDTO.getUsername())
                   .role(role)
                   .printer(userDTO.getPrinter())
                   .resetPassword(triggerReset)
                   .build();
    }
    
}