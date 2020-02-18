package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.RoleDTO;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.UserDTO;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserServiceTest {

    private final static String USERNAME = "nhabruker1";

    @Inject
    private UserServiceInterface userService;

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Test
    public void createUser_newAdmin_shouldReturnNewUser() {
        final UserDTO validUser = generateUser();
        validUser.setPassword("testpass");

        final UserDTO created = userService.createUser(validUser);

        assertNotNull(created);
    }

    @Test(expected = ValidationErrorException.class)
    public void createUser_emptyPassword_shouldThrowValidationError() {
        final UserDTO errorUser = generateUser();

        userService.createUser(errorUser);
    }

    @Test(expected = ValidationErrorException.class)
    public void createUser_shortPassword_shouldThrowValidationError() {
        final UserDTO errorUser = generateUser();
        errorUser.setPassword("test");

        userService.createUser(errorUser);
    }

    @Test(expected = ValidationErrorException.class)
    public void createUser_invalidIP_shouldThrowValidationError() {
        final UserDTO errorUser = generateUser();
        errorUser.setPassword("validpass");
        errorUser.setPrinter("invalid");

        userService.createUser(errorUser);
    }

    @Test
    public void updateUser_newPassword_shouldReturnValidUser() {
        final UserDTO existingUser = userService.getUser("nhabruker2");
        existingUser.setPassword("newpassword");
        existingUser.setPasswordConfirm("newpassword");

        final UserDTO updated = userService.updateUser(existingUser, USERNAME);

        assertNotNull(updated);
    }

    @Test(expected = ValidationErrorException.class)
    public void updateUser_invalidPassword_shouldThrowValidationError() {
        final UserDTO existingUser = userService.getUser("nhabruker2");
        existingUser.setPassword("test");
        existingUser.setPasswordConfirm("test");
        existingUser.setResetPassword(false);
        
        userService.updateUser(existingUser, USERNAME);
    }

    @Test(expected = ValidationErrorException.class)
    public void updateUser_invalidIP_shouldThrowValidationError() {
        final UserDTO existingUser = userService.getUser("nhabruker2");
        existingUser.setPrinter("localhost");
        
        userService.updateUser(existingUser, USERNAME);
        existingUser.setPrinter("127.0.0.1");
    }

    @Test(expected = ValidationErrorException.class)
    public void updateUser_nonExistentUser_shouldThrowValidationError() {
        final UserDTO nonExistentUser = generateUser();
        nonExistentUser.setUsername("doesnotexist");

        userService.updateUser(nonExistentUser, USERNAME);
    }

    @Test
    public void updateUser_setResetPassword_shouldReturnUpdatedUser() {
        final UserDTO existingUser = userService.getUser("nhabruker2");
        existingUser.setResetPassword(true);

        final UserDTO updated = userService.updateUser(existingUser, USERNAME);
        
        assertNotNull(updated);
        assertEquals(true, updated.getResetPassword());
    }
    
    @Test
    public void updateUser_updateRole_ownRole_shouldReturnOldRole() {
        final UserDTO existingUser = userService.getUser("nhabruker1");
        existingUser.setRole(new RoleDTO("bruker"));
        
        final UserDTO updated = userService.updateUser(existingUser, USERNAME);

        assertNotNull(updated);
        assertNotNull(updated.getRole());
        assertEquals("admin", updated.getRole().getName());
    }
    
    @Test
    public void getUsers_shouldReturnThreeOrMoreUsers() {
        final List<UserDTO> userDTOList = userService.getUsers();
        
        assertNotNull(userDTOList);
        assertTrue(userDTOList.size() >= 3);
    }

    @Test
    public void getRoles_shouldReturnTwoRoles() {
        final List<RoleDTO> roles = userService.getRoles();
        
        assertNotNull(roles);
        assertEquals(2, roles.size());
    }

    @Test
    public void checkPasswordReset_shouldReturnTrue() {
        final UserDTO existingUser = userService.getUser("nhabruker2");
        existingUser.setResetPassword(true);
        System.out.println(existingUser.getPrinter());
        userService.updateUser(existingUser, USERNAME);

        final Boolean passwordReset = userService.checkPasswordReset("nhabruker2");
        assertNotNull(passwordReset);
        assertEquals(true, passwordReset);
    }

    @Test
    public void getRole_adminUser_shouldReturnAdminRole() {
        final String role = userService.getRole(USERNAME);
        
        assertNotNull(role);
        assertEquals("admin", role);
    }

    private UserDTO generateUser() {
        return UserDTO.builder()
                      .username("TestUser")
                      .printer("127.0.0.1")
                      .role(new RoleDTO("admin"))
                      .build();
    }

}