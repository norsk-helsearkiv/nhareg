package no.arkivverket.helsearkiv.nhareg.tjeneste;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;

import static org.junit.Assert.assertNotNull;

import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import no.arkivverket.helsearkiv.nhareg.utilities.UserHandler;

@RunWith(Arquillian.class)
public class KjonnTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private KjønnTjeneste tjeneste;

    @Inject
    private UserHandler userHandler;
    
    @Test
    public void create_gyldigKjonn_skalIkkeGiNull() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Kjønn kjonn = new Kjønn();
                kjonn.setCode("I");
                kjonn.setDisplayName("Intetkjønn");
                Kjønn result = tjeneste.create(kjonn);
                assertNotNull(result);
                
                return null;
            }
        });
    }
}
