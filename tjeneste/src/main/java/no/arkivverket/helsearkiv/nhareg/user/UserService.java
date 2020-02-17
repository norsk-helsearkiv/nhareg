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
import java.util.*;

public class UserService implements UserServiceInterface {

    @Inject
    private UserDAO userDAO;

    @Inject
    private UserConverterInterface userConverter;

    @Override 
    public UserDTO createUser(final UserDTO userDTO, final String username) {
        validatePrinterIP(userDTO.getPrinter());
        validatePassword(userDTO.getPassword());
        
        final User user = userConverter.toUser(userDTO);
        final String hashedPassword = passwordToHash(userDTO.getPassword());
        user.setPassword(hashedPassword);
        
        final User newUser = userDAO.create(user); 
        
        return userConverter.fromUser(newUser);
    }

    @Override
    public UserDTO updateUser(final UserDTO userDTO, final String username) {
        final Boolean resetPassword = userDTO.getResetPassword();
        boolean resetPass = resetPassword != null && resetPassword;
        final String password = userDTO.getPassword();
        
        validatePrinterIP(userDTO.getPrinter());

        final User original = userDAO.fetchById(userDTO.getUsername());
        final User user = userConverter.toUser(userDTO);

        if (original == null) {
            final ValidationError validationError = new ValidationError("username", "UserDoesNotExist");
            final List<ValidationError> validationErrors = Collections.singletonList(validationError);
            throw new ValidationErrorException(validationErrors);
        }
        
        // if password is empty or we are resetting the password on login we do
        // not need to validate the password as it will not get updated.
        if ((password != null && !password.isEmpty()) && !resetPass) {
            validatePassword(password);
            final String hashedPassword = passwordToHash(password);
            user.setPassword(hashedPassword);
        } else {
            user.setPassword(original.getPassword());
        }

        // Default to "user" role.
        final String name = user.getRole().getName();
        if (name == null || name.isEmpty()) {
            user.getRole().setName(Roles.ROLE_USER);
        }

        // An admin cannot change their own role.
        final String loggedInRole = userDAO.getRole(username);
        if (loggedInRole.equals("admin") && userDTO.getUsername().equals(username)) {
            user.getRole().setName(loggedInRole);
        }

        final User updatedUser = userDAO.update(user);
        
        return userConverter.fromUser(updatedUser);
    }

    @Override
    public void updatePassword(final String newPassword, final String username) {
        validatePassword(newPassword);

        final User user = userDAO.fetchById(username);
        final String hashedPassword = passwordToHash(newPassword);
        user.setPassword(hashedPassword);
        user.setResetPassword("");
    }

    @Override
    public List<UserDTO> getUsers() {
        final List<User> users = userDAO.getAllUsers();

        return userConverter.fromUserList(users);
    }

    @Override
    public List<Role> getRoles() {
        return userDAO.getRoles();
    }

    @Override
    public Boolean checkPasswordReset(final String username) {
        final User user = userDAO.fetchById(username);

        return "Y".equals(user.getResetPassword());
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

    private void validatePassword(final String password) {
        if (password == null || password.length() < 5) {
            final ValidationError validationError = new ValidationError("passord", "FeilPassord");
            final List<ValidationError> validationErrors = Collections.singletonList(validationError);
            throw new ValidationErrorException(validationErrors);
        }
    }

    private void validatePrinterIP(final String printerIP) {
        final List<ValidationError> errorList = new ArrayList<>();

        if (printerIP == null || printerIP.isEmpty()) {
            final ValidationError emptyPrinterError = new ValidationError("printer", "IPEmpty");
            errorList.add(emptyPrinterError);
        } else {
            final String[] ipGroups = printerIP.split("\\.");
            if (ipGroups.length != 4) {
                errorList.add(new ValidationError("printer", "IPLength"));
            }

            try {
                boolean correctFormat = Arrays.stream(ipGroups)
                                              .filter(ipGroup -> ipGroup.length() > 0)
                                              .map(Integer::parseInt)
                                              .filter(group -> (group >= 0 && group <= 255))
                                              .count() == 4;
                if (!correctFormat) {
                    errorList.add(new ValidationError("printer", "IPFormat"));
                }
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                errorList.add(new ValidationError("printer", "IPIntegers"));
            }
        }

        if (errorList.size() > 0) {
            throw new ValidationErrorException(errorList);
        }
    }

}