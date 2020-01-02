package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.BrukerDTO;

import java.util.List;

public interface UserServiceInterface {

    BrukerDTO updateUser(final BrukerDTO userDTO, final String username);

    void updatePassword(final String newPassword, final String username);

    String getLastUsedStorageUnit(final String username);

    String getRole(final String username);

    List<BrukerDTO> getUsers();

    List<Rolle> getRoles();

    Boolean checkPasswordReset(final String username);

    Bruker getByUsername(final String username);

    void updateDefaultTransferForUser(final String username, final String transferId);
    
}