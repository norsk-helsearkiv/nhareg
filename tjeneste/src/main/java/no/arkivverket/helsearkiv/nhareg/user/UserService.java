package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.common.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.BrukerDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
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

    @Resource
    private SessionContext sessionContext;

    @Override
    public BrukerDTO updateUser(BrukerDTO userDTO) {
        final Bruker bruker = userDTO.toBruker();

        //defaulter til bruker-rolle hvis det mangler..
        if (bruker.getRolle().getNavn().isEmpty()) {
            bruker.getRolle().setNavn(Roles.ROLE_BRUKER);
        }

        //admin bruker kan ikke endre rolle på seg selv... bare overskriver i første omgang, kan forfines ved behov...
        final String username = sessionContext.getCallerPrincipal().getName();
        final String loggedInRolle = userDAO.getRolle(username);
        if (loggedInRolle.equals("admin") && userDTO.getBrukernavn().equals(username)) {
            bruker.getRolle().setNavn(loggedInRolle);
        }

        boolean resetPass = false;
        if (userDTO.getResetPassword() != null && userDTO.getResetPassword()) {
            bruker.setResetPassord("Y"); // enkel resetpassord-indikator kan forfines ved behov...
            resetPass = true;
        } else {
            bruker.setResetPassord("");
        }

        if (!resetPass) { // lite poeng å validere passord hvis det skal resettes
            List<Valideringsfeil> feil = validerNyEndreBruker(userDTO.getPassword());
            if (feil.size() > 0) {
                throw new ValideringsfeilException(feil);
            }
        }

        validatePrinterIP(bruker.getPrinterzpl());

        String b64Pwd = passordToHash(bruker.getPassord());
        bruker.setPassord(b64Pwd);

        final Bruker newUser = userDAO.createBruker(bruker);
        return new BrukerDTO(newUser);
    }

    @Override
    public void updatePassword(final String newPassword) {
        final String username = sessionContext.getCallerPrincipal().getName();
        final Bruker bruker = userDAO.fetchByUsername(username);
        final String b64pwd = passordToHash(newPassword);

        final List<Valideringsfeil> feil = validerNyEndreBruker(newPassword);
        if (feil.size() > 0) {
            throw new ValideringsfeilException(feil);
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
    public Boolean checkPasswordReset() {
        final String username = sessionContext.getCallerPrincipal().getName();
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
    public String getUser() {
        return sessionContext.getCallerPrincipal().getName();
    }

    @Override
    public String getRole() {
        final String username = sessionContext.getCallerPrincipal().getName();

        return userDAO.getRolle(username);
    }

    @Override
    public String getLastUsedStorageUnit() {
        final String username = sessionContext.getCallerPrincipal().getName();
        return userDAO.fetchStorageUnitByUsername(username);
    }

    private List<Valideringsfeil> validerNyEndreBruker(final String passord) {
        List<Valideringsfeil> feilList = new ArrayList<Valideringsfeil>();
        if (!validerPassord(passord)) {
            Valideringsfeil feil = new Valideringsfeil("passord", "FeilPassord");
            feilList.add(feil);
        }

        return feilList;
    }

    private boolean validerPassord(String passord) {
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
        final List<Valideringsfeil> errorList = new ArrayList<>();

        if (printerIP == null || printerIP.isEmpty()) {
            final Valideringsfeil emptyPrinterError =new Valideringsfeil("printer", "Empty printer IP");
            errorList.add(emptyPrinterError);
        } else {
            final String[] ipGroups = printerIP.split("\\.");
            if (ipGroups.length != 4) {
                errorList.add(new Valideringsfeil("printer", "Error in IP address length"));
            }

            try {
                boolean correctFormat = Arrays.stream(ipGroups)
                                              .filter(group -> group.length() > 1 && group.startsWith("0"))
                                              .map(Integer::parseInt)
                                              .filter(group -> (group >= 0 && group <= 255))
                                              .count() == 4;
                if (!correctFormat) {
                    errorList.add(new Valideringsfeil("printer", "Error in IP format"));
                }
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                errorList.add(new Valideringsfeil("printer", "Error with integers in IP"));
            }
        }

        if (errorList.size() > 0) {
            throw new ValideringsfeilException(errorList);
        }
    }
}