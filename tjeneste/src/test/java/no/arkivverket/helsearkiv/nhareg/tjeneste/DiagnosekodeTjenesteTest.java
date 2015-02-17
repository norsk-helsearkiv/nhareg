package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.util.RESTDeployment;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DiagnosekodeTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private DiagnosekodeTjeneste tjeneste;

    @Test
    public void create() {
        Diagnosekode diagnosekode = new Diagnosekode();
        diagnosekode.setCode("Diagnosekode100");
        diagnosekode.setCodeSystem("Kodesystem99");
        diagnosekode.setCodeSystemVersion("0.9.Beta");
        diagnosekode.setDisplayName("Influensa beta");
        Diagnosekode response = tjeneste.create(diagnosekode);
        assertNotNull(response);
    }

    @Test
    public void getAllUtenPaginering() {
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
        List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
        assertNotNull(diagnosekoder);
        assertEquals(3, diagnosekoder.size());
    }

    @Test
    public void getAllMedPaginering() {
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
        queryParameters.add(EntitetsTjeneste.SIDE, "1");
        queryParameters.add(EntitetsTjeneste.ANTALL, "15");
        List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
        assertNotNull(diagnosekoder);
        assertEquals(3, diagnosekoder.size());
    }

    @Test
    public void hentDiagnosekoderMedCode() {
        String code = "Code0";
        List<Diagnosekode> diagnosekoder = tjeneste.hentDiagnosekoderMedCode(code);
        assertNotNull(diagnosekoder);
        assertEquals(1, diagnosekoder.size());
    }

    @Test
    public void hentDiagnosekoderMedUkjentCode() {
        String code = "Ukjent";
        List<Diagnosekode> diagnosekoder = tjeneste.hentDiagnosekoderMedCode(code);
        assertNotNull(diagnosekoder);
        assertEquals(0, diagnosekoder.size());
    }
    @Test
    public void hentDiagnosekoderMedNulltCode() {
        String code = null;
        List<Diagnosekode> diagnosekoder = tjeneste.hentDiagnosekoderMedCode(code);
        assertNotNull(diagnosekoder);
        assertEquals(0, diagnosekoder.size());
    }
}
