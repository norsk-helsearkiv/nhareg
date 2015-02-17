package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.util.RESTDeployment;
import java.util.List;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class LagringsenhetTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private LagringsenhetTjeneste tjeneste;

    @Test(expected = ValideringsfeilException.class)
    public void createUtenIdentifikator() {
        Lagringsenhet lagringsenhet = new Lagringsenhet();
        tjeneste.create(lagringsenhet);
    }

    @Test
    public void create() {
        Lagringsenhet lagringsenhet = new Lagringsenhet();
        lagringsenhet.setIdentifikator("LagringsenhetForEnhetstesting");
        Lagringsenhet lgt = tjeneste.create(lagringsenhet);
        assertNotNull(lgt);
    }

    @Test
    public void hentLagringsenhetMedIdentifikator() {
        Lagringsenhet l = tjeneste.hentLagringsenhetMedIdentifikator("boks1");
        assertNotNull(l);
    }

    @Test
    public void hentLagringsenheterForAvlevering() {
        String avleveringsidentifikator = "Avlevering-1";
        List<Lagringsenhet> lagringsenheter = tjeneste.hentLagringsenheterForAvlevering(avleveringsidentifikator);
        assertNotNull(lagringsenheter);
        assertEquals(3, lagringsenheter.size());
    }

    @Test
    public void hentLagringsenheterForUkjentAvlevering() {
        String avleveringsidentifikator = "Avlevering-ukjent";
        List<Lagringsenhet> lagringsenheter = tjeneste.hentLagringsenheterForAvlevering(avleveringsidentifikator);
        assertNotNull(lagringsenheter);
        assertEquals(0, lagringsenheter.size());
    }
}
