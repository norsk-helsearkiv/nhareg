package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.auth.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Role;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.UserDTO;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class UserService implements UserServiceInterface {

    @Inject
    private UserDAO userDAO;

    @Inject
    private UserConverterInterface userConverter;
    
    @Override
    public UserDTO updateUser(final UserDTO userDTO, final String username) {
        final User user = userConverter.toUser(userDTO);

        //defaulter til bruker-rolle hvis det mangler..
        final String userName = user.getRole().getName();
        if (userName != null && userName.isEmpty()) {
            user.getRole().setName(Roles.ROLE_USER);
        }

        //admin bruker kan ikke endre rolle på seg selv... bare overskriver i første omgang, kan forfines ved behov...
        final String loggedInRole = userDAO.getRole(username);
        if (loggedInRole.equals("admin") && userDTO.getUsername().equals(username)) {
            user.getRole().setName(loggedInRole);
        }

        boolean resetPass = false;
        if (userDTO.getResetPassword() != null && userDTO.getResetPassword()) {
            user.setResetPassword("Y"); // enkel resetpassord-indikator kan forfines ved behov...
            resetPass = true;
        } else {
            user.setResetPassword("");
        }

        if (!resetPass) { // lite poeng å validere passord hvis det skal resettes
            List<ValidationError> feil = validateNewChangeUser(userDTO.getPassword());
            if (feil.size() > 0) {
                throw new ValidationErrorException(feil);
            }
        }

        validatePrinterIP(user.getPrinter());

        String b64Pwd = passwordToHash(user.getPassword());
        user.setPassword(b64Pwd);

        final User newUser = userDAO.createUser(user);
        
        return userConverter.fromUser(newUser);
    }

    @Override
    public void updatePassword(final String newPassword, final String username) {
        final User user = userDAO.fetchByUsername(username);
        final String b64pwd = passwordToHash(newPassword);

        final List<ValidationError> feil = validateNewChangeUser(newPassword);
        if (feil.size() > 0) {
            throw new ValidationErrorException(feil);
        }

        user.setPassword(b64pwd);
        user.setResetPassword("");
    }

    @Override
    public List<UserDTO> getUsers() {
        final List<UserDTO> dtos = new ArrayList<>();
        for (User user : userDAO.getAllUsers()) {
            final UserDTO userDto = userConverter.fromUser(user);
            dtos.add(userDto);
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

        return "Y".equals(user.getResetPassword());
    }

    @Override
    public User getByUsername(final String username) {
        return userDAO.fetchByUsername(username);
    }

    @Override 
    public void updateDefaultTransferForUser(final String username, final String transferId) {
        userDAO.updateDefaultTransfer(username, transferId);
    }

    @Override
    public String getRole(final String username) {
        return userDAO.getRole(username);
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
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(hash);
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