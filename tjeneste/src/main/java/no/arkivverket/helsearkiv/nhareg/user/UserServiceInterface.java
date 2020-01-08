package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Role;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.BrukerDTO;

import java.util.List;

public interface UserServiceInterface {

    BrukerDTO updateUser(final BrukerDTO userDTO, final String username);

    void updatePassword(final String newPassword, final String username);

    String getLastUsedStorageUnit(final String username);

    String getRole(final String username);

    List<BrukerDTO> getUsers();

    List<Role> getRoles();

    Boolean checkPasswordReset(final String username);

    User getByUsername(final String username);

    void updateDefaultTransferForUser(final String username, final String transferId);
    
}