package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Role;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.UserDTO;

import java.util.List;

public interface UserServiceInterface {

    UserDTO updateUser(final UserDTO userDTO, final String username);

    void updatePassword(final String newPassword, final String username);

    String getLastUsedStorageUnit(final String username);

    String getRole(final String username);

    List<UserDTO> getUsers();

    List<Role> getRoles();

    Boolean checkPasswordReset(final String username);

    User getByUsername(final String username);

    void updateDefaultTransferForUser(final String username, final String transferId);
    
}