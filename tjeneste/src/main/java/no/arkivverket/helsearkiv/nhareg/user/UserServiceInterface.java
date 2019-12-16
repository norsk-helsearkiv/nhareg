package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.BrukerDTO;

import java.util.List;

public interface UserServiceInterface {

    BrukerDTO updateUser(BrukerDTO userDTO);

    void updatePassword(final String newPassword);

    String getLastUsedStorageUnit();

    String getUser();

    String getRole();

    List<BrukerDTO> getUsers();

    List<Rolle> getRoles();

    Boolean checkPasswordReset();

    Bruker getByUsername(final String username);

    void updateDefaultTransferForUser(final String username, final String transferId);
    
}