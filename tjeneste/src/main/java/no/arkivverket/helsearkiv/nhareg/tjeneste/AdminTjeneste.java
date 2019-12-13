package no.arkivverket.helsearkiv.nhareg.tjeneste;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import no.arkivverket.helsearkiv.nhareg.auth.Roller;
import no.arkivverket.helsearkiv.nhareg.auth.UserService;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.BrukerDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;

/**
 * Created by haraldk on 15.04.15.
 */
@Path("/admin")
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
@Stateless
public class AdminTjeneste {

    @EJB
    private UserService userService;
    @EJB
    private KonfigparamTjeneste konfigparam;
    @Resource
    private SessionContext sessionContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/rolle")
    public String getRolle() {
        final String username = sessionContext.getCallerPrincipal().getName();
        return userService.getRolle(username);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/bruker")
    public String getBruker() {
        return sessionContext.getCallerPrincipal().getName();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/century")
    public String getCentury() {
        return konfigparam.getVerdi(KonfigparamTjeneste.KONFIG_AARHUNDRE);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/resetPassord")
    public Boolean checkPassordReset() {
        final String username = sessionContext.getCallerPrincipal().getName();
        Bruker b = userService.findByUsername(username);
        return "Y".equals(b.getResetPassord());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/oppdaterPassord")
    public Response oppdaterPassord(final String nyttPassord) {
        final String username = sessionContext.getCallerPrincipal().getName();
        Bruker bruker = userService.findByUsername(username);
        String b64pwd = passordToHash(nyttPassord);
        List<Valideringsfeil> feil = validerNyEndreBruker(nyttPassord);

        if (feil.size() > 0) {
            throw new ValideringsfeilException(feil);
        }
        bruker.setPassord(b64pwd);
        bruker.setResetPassord("");

        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roller")
    public List<Rolle> getRoller() {
        return userService.getRoller();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roller.ROLE_ADMIN})
    @Path("/brukere")
    public List<BrukerDTO> getBrukere() {
        List<BrukerDTO> dtos = new ArrayList<BrukerDTO>();
        for (Bruker bruker: userService.getAllBrukere()) {
            dtos.add(new BrukerDTO(bruker));
        }

        return dtos;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roller.ROLE_ADMIN})
    @Path("/brukere")
    public Response oppdaterBruker(BrukerDTO brukerDTO) {
        Bruker bruker = brukerDTO.toBruker();
        //defaulter til bruker-rolle hvis det mangler..
        if (bruker.getRolle().getNavn().isEmpty()) {
            bruker.getRolle().setNavn("bruker");
        }

        //admin bruker kan ikke endre rolle på seg selv... bare overskriver i første omgang, kan forfines ved behov...
        final String username = sessionContext.getCallerPrincipal().getName();
        String loggedInRolle = getRolle();
        if (loggedInRolle.equals("admin") && brukerDTO.getBrukernavn().equals(username)) {
            bruker.getRolle().setNavn(loggedInRolle);
        }

        boolean resetPass = false;
        if (brukerDTO.getResetPassword() != null && brukerDTO.getResetPassword()) {
            bruker.setResetPassord("Y"); // enkel resetpassord-indikator kan forfines ved behov...
            resetPass = true;
        } else {
            bruker.setResetPassord("");
        }

        if (!resetPass) { // lite poeng å validere passord hvis det skal resettes
            List<Valideringsfeil> feil = validerNyEndreBruker(brukerDTO.getPassword());
            if (feil.size() > 0) {
                throw new ValideringsfeilException(feil);
            }
        }

        validatePrinterIP(bruker.getPrinterzpl());

        String b64Pwd = passordToHash(bruker.getPassord());
        bruker.setPassord(b64Pwd);

        Bruker ny = userService.createBruker(bruker);

        return Response.ok(new BrukerDTO(ny)).build();
    }

    private String passordToHash(final String passord) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(passord.getBytes("UTF-8"));
            return Base64.encodeBase64String(hash);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean validerPassord(String passord) {
        return passord != null && passord.length() >= 5;
    }

    private List<Valideringsfeil> validerNyEndreBruker(final String passord) {
        List<Valideringsfeil> feilList = new ArrayList<Valideringsfeil>();
        if (!validerPassord(passord)) {
            Valideringsfeil feil = new Valideringsfeil("passord", "FeilPassord");
            feilList.add(feil);
        }

        return feilList;
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

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sistBrukte")
    public String getSistBrukteLagringsenhet() {
        final String username = sessionContext.getCallerPrincipal().getName();

        return userService.getLagringsenhet(username);
    }

    public void clearCache(String username) {
        try {
            ObjectName jaasMgr = new ObjectName("jboss.as:subsystem=security,security-domain=<YOUR SECURITY DOMAIN>");
            Object[] params = { username };
            String[] signature = {"java.lang.String"};
            MBeanServer server = MBeanServerFactory.findMBeanServer(null).get(0);
            server.invoke(jaasMgr, "flushCache", params, signature);
        } catch (Exception ignored) {}
    }
}
