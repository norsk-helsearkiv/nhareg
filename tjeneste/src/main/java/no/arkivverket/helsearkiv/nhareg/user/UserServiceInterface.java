package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.RoleDTO;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.UserDTO;

import java.util.List;

public interface UserServiceInterface {

    UserDTO createUser(final UserDTO userDTO);
    
    UserDTO updateUser(final UserDTO userDTO, final String username);
    
    UserDTO getUser(final String username);

    void updatePassword(final String newPassword, final String username);

    String getLastUsedStorageUnit(final String username);

    String getRole(final String username);

    List<UserDTO> getUsers();

    List<RoleDTO> getRoles();

    boolean checkPasswordReset(final String username);

    void updateDefaultTransferForUser(final String username, final String transferId);

}