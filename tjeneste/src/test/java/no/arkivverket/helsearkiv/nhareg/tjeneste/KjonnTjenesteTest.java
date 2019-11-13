package no.arkivverket.helsearkiv.nhareg.tjeneste;

import org.jboss.arquillian.junit.Arquillian;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class KjonnTjenesteTest {


    @Test
    public void dummy() {
        assert(true);
    }
//    @Deployment
//    public static WebArchive deployment() {
//        return RESTDeployment.deployment();
//    }
//
//    @Inject
//    private KjønnTjeneste tjeneste;
//
//    @Test
//    public void create() {
//        Kjønn kjønn = new Kjønn();
//        kjønn.setCode("I");
//        kjønn.setDisplayName("Intetkjønn");
//        Kjønn rsp = tjeneste.create(kjønn);
//        assertNotNull(rsp);
//    }
}
