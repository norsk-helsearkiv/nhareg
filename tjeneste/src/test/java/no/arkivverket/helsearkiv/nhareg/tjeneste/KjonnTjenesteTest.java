package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.util.RESTDeployment;

import javax.inject.Inject;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class KjonnTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private KjønnTjeneste tjeneste;

    @Test
    public void create() {
        Kjønn kjønn = new Kjønn();
        kjønn.setCode("I");
        kjønn.setDisplayName("Intetkjønn");
        Kjønn rsp = tjeneste.create(kjønn);
        assertNotNull(rsp);
    }
}
