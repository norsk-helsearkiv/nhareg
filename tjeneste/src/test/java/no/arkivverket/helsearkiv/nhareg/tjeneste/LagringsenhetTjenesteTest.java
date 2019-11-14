package no.arkivverket.helsearkiv.nhareg.tjeneste;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import no.arkivverket.helsearkiv.nhareg.utilities.UserHandler;

@RunWith(Arquillian.class)
public class LagringsenhetTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private LagringsenhetTjeneste tjeneste;
    
    @Inject
    private UserHandler userHandler;

    @Test(expected = ValideringsfeilException.class)
    public void create_utenIdentifikator_skalKasteValideringsfeilException() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Lagringsenhet lagringsenhet = new Lagringsenhet();
                tjeneste.create(lagringsenhet);
                
                return null;
            }
        });
    }

    @Test
    public void create_medGyldigIdentifikator_skalIkkeGiNull() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Lagringsenhet lagringsenhet = new Lagringsenhet();
                lagringsenhet.setIdentifikator("LagringsenhetForEnhetstesting");
                Lagringsenhet nyLagringsenhet = tjeneste.create(lagringsenhet);
                assertNotNull(nyLagringsenhet);
           
                return null;
            }
        });
    }

    @Test
    public void hentLagringsenhetMedIdentifikator_gyldigId_skalReturnereLagringsenhet() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Lagringsenhet lagringsenhet = tjeneste.hentLagringsenhetMedIdentifikator("boks1");
                assertNotNull(lagringsenhet);
                
                return null;
            }
        });
    }

    @Test
    public void hentLagringsenheterForAvlevering_gyldigId_skalReturnereTre() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                final String avleveringsidentifikator = "Avlevering-1";
                List<Lagringsenhet> lagringsenheter = tjeneste.hentLagringsenheterForAvlevering(avleveringsidentifikator);
                assertNotNull(lagringsenheter);
                assertEquals(3, lagringsenheter.size());
                
                return null;
            }
        });
    }

    @Test
    public void hentLagringsenheterForAvlevering_ukjentAvlevering_skalReturnereNull() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                String avleveringsidentifikator = "Avlevering-ukjent";
                List<Lagringsenhet> lagringsenheter = tjeneste.hentLagringsenheterForAvlevering(avleveringsidentifikator);
                assertNotNull(lagringsenheter);
                assertEquals(0, lagringsenheter.size());
                
                return null;
            }
        });
    }
}
