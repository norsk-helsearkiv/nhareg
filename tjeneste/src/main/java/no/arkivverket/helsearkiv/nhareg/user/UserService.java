package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.common.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.BrukerDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValidationErrorException;
import org.apache.commons.codec.binary.Base64;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService implements UserServiceInterface {

    @Inject
    private UserDAO userDAO;

    @Override
    public BrukerDTO updateUser(final BrukerDTO userDTO, final String username) {
        final Bruker bruker = userDTO.toBruker();

        //defaulter til bruker-rolle hvis det mangler..
        final String userName = bruker.getRolle().getNavn();
        if (userName != null && userName.isEmpty()) {
            bruker.getRolle().setNavn(Roles.ROLE_USER);
        }

        //admin bruker kan ikke endre rolle på seg selv... bare overskriver i første omgang, kan forfines ved behov...
        final String loggedInRole = userDAO.getRolle(username);
        if (loggedInRole.equals("admin") && userDTO.getBrukernavn().equals(username)) {
            bruker.getRolle().setNavn(loggedInRole);
        }

        boolean resetPass = false;
        if (userDTO.getResetPassword() != null && userDTO.getResetPassword()) {
            bruker.setResetPassord("Y"); // enkel resetpassord-indikator kan forfines ved behov...
            resetPass = true;
        } else {
            bruker.setResetPassord("");
        }

        if (!resetPass) { // lite poeng å validere passord hvis det skal resettes
            List<ValidationError> feil = validerNyEndreBruker(userDTO.getPassword());
            if (feil.size() > 0) {
                throw new ValidationErrorException(feil);
            }
        }

        validatePrinterIP(bruker.getPrinterzpl());

        String b64Pwd = passordToHash(bruker.getPassord());
        bruker.setPassord(b64Pwd);

        final Bruker newUser = userDAO.createBruker(bruker);
        return new BrukerDTO(newUser);
    }

    @Override
    public void updatePassword(final String newPassword, final String username) {
        final Bruker bruker = userDAO.fetchByUsername(username);
        final String b64pwd = passordToHash(newPassword);

        final List<ValidationError> feil = validerNyEndreBruker(newPassword);
        if (feil.size() > 0) {
            throw new ValidationErrorException(feil);
        }

        bruker.setPassord(b64pwd);
        bruker.setResetPassord("");
    }

    @Override
    public List<BrukerDTO> getUsers() {
        List<BrukerDTO> dtos = new ArrayList<BrukerDTO>();
        for (Bruker bruker: userDAO.getAllBrukere()) {
            dtos.add(new BrukerDTO(bruker));
        }

        return dtos;
    }

    @Override
    public List<Rolle> getRoles() {
        return userDAO.getRoller();
    }

    @Override
    public Boolean checkPasswordReset(final String username) {
        final Bruker user = userDAO.fetchByUsername(username);

        return "Y".equals(user.getResetPassord());
    }

    @Override
    public Bruker getByUsername(String username) {
        return userDAO.fetchByUsername(username);
    }

    @Override 
    public void updateDefaultTransferForUser(final String username, final String transferId) {
        userDAO.updateDefaultAvlevering(username, transferId);
    }

    @Override
    public String getRole(final String username) {
        return userDAO.getRolle(username);
    }

    @Override
    public String getLastUsedStorageUnit(final String username) {
        return userDAO.fetchStorageUnitByUsername(username);
    }

    private List<ValidationError> validerNyEndreBruker(final String passord) {
        List<ValidationError> feilList = new ArrayList<ValidationError>();
        if (!validerPassord(passord)) {
            ValidationError feil = new ValidationError("passord", "FeilPassord");
            feilList.add(feil);
        }

        return feilList;
    }

    private boolean validerPassord(final String passord) {
        return passord != null && passord.length() >= 5;
    }

    private String passordToHash(final String passord) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(passord.getBytes(StandardCharsets.UTF_8));

            return Base64.encodeBase64String(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void validatePrinterIP(final String printerIP) {
        final List<ValidationError> errorList = new ArrayList<>();

        if (printerIP == null || printerIP.isEmpty()) {
            final ValidationError emptyPrinterError = new ValidationError("printer", "Empty printer IP");
            errorList.add(emptyPrinterError);
        } else {
            final String[] ipGroups = printerIP.split("\\.");
            if (ipGroups.length != 4) {
                errorList.add(new ValidationError("printer", "Error in IP address length"));
            }

            try {
                boolean correctFormat = Arrays.stream(ipGroups)
                                              .filter(group -> group.length() > 1 && group.startsWith("0"))
                                              .map(Integer::parseInt)
                                              .filter(group -> (group >= 0 && group <= 255))
                                              .count() == 4;
                if (!correctFormat) {
                    errorList.add(new ValidationError("printer", "Error in IP format"));
                }
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                errorList.add(new ValidationError("printer", "Error with integers in IP"));
            }
        }

        if (errorList.size() > 0) {
            throw new ValidationErrorException(errorList);
        }
    }

}