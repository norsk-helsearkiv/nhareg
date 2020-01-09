package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.common.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Role;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.BrukerDTO;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;
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
        final User user = userDTO.toBruker();

        //defaulter til bruker-rolle hvis det mangler..
        final String userName = user.getRole().getName();
        if (userName != null && userName.isEmpty()) {
            user.getRole().setName(Roles.ROLE_USER);
        }

        //admin bruker kan ikke endre rolle på seg selv... bare overskriver i første omgang, kan forfines ved behov...
        final String loggedInRole = userDAO.getRolle(username);
        if (loggedInRole.equals("admin") && userDTO.getBrukernavn().equals(username)) {
            user.getRole().setName(loggedInRole);
        }

        boolean resetPass = false;
        if (userDTO.getResetPassword() != null && userDTO.getResetPassword()) {
            user.setResetPassord("Y"); // enkel resetpassord-indikator kan forfines ved behov...
            resetPass = true;
        } else {
            user.setResetPassord("");
        }

        if (!resetPass) { // lite poeng å validere passord hvis det skal resettes
            List<ValidationError> feil = validateNewChangeUser(userDTO.getPassword());
            if (feil.size() > 0) {
                throw new ValidationErrorException(feil);
            }
        }

        validatePrinterIP(user.getPrinterzpl());

        String b64Pwd = passwordToHash(user.getPassord());
        user.setPassord(b64Pwd);

        final User newUser = userDAO.createBruker(user);
        return new BrukerDTO(newUser);
    }

    @Override
    public void updatePassword(final String newPassword, final String username) {
        final User user = userDAO.fetchByUsername(username);
        final String b64pwd = passwordToHash(newPassword);

        final List<ValidationError> feil = validateNewChangeUser(newPassword);
        if (feil.size() > 0) {
            throw new ValidationErrorException(feil);
        }

        user.setPassord(b64pwd);
        user.setResetPassord("");
    }

    @Override
    public List<BrukerDTO> getUsers() {
        final List<BrukerDTO> dtos = new ArrayList<>();
        for (User user : userDAO.getAllBrukere()) {
            dtos.add(new BrukerDTO(user));
        }

        return dtos;
    }

    @Override
    public List<Role> getRoles() {
        return userDAO.getRoller();
    }

    @Override
    public Boolean checkPasswordReset(final String username) {
        final User user = userDAO.fetchByUsername(username);

        return "Y".equals(user.getResetPassord());
    }

    @Override
    public User getByUsername(final String username) {
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

    private List<ValidationError> validateNewChangeUser(final String password) {
        final List<ValidationError> validationErrors = new ArrayList<>();
        
        if (!validatePassword(password)) {
            ValidationError validationError = new ValidationError("passord", "FeilPassord");
            validationErrors.add(validationError);
        }

        return validationErrors;
    }

    private boolean validatePassword(final String password) {
        return password != null && password.length() >= 5;
    }

    private String passwordToHash(final String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

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