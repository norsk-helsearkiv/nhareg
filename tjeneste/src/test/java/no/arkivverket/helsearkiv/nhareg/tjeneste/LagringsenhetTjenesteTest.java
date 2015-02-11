package no.arkivverket.helsearkiv.nhareg.tjeneste;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;

@RunWith(Arquillian.class)
public class LagringsenhetTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private LagringsenhetTjeneste tjeneste;

    @Test(expected = RuntimeException.class)
    public void createUtenIdentifikator() {
        Lagringsenhet lagringsenhet = new Lagringsenhet();
        Response response = tjeneste.create(lagringsenhet);
        assertNotNull(response);
    }

    @Test
    public void create() {
        Lagringsenhet lagringsenhet = new Lagringsenhet();
        lagringsenhet.setIdentifikator("LagringsenhetForEnhetstesting");
        Response response = tjeneste.create(lagringsenhet);
        assertNotNull(response);
    }

}
