package no.arkivverket.helsearkiv.nhareg.tjeneste;

import org.jboss.arquillian.junit.Arquillian;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class LagringsenhetTjenesteTest {

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
//    private LagringsenhetTjeneste tjeneste;
//
//    @Test(expected = ValideringsfeilException.class)
//    public void createUtenIdentifikator() {
//        Lagringsenhet lagringsenhet = new Lagringsenhet();
//        tjeneste.create(lagringsenhet);
//    }
//
//    @Test
//    public void create() {
//        Lagringsenhet lagringsenhet = new Lagringsenhet();
//        lagringsenhet.setIdentifikator("LagringsenhetForEnhetstesting");
//        Lagringsenhet lgt = tjeneste.create(lagringsenhet);
//        assertNotNull(lgt);
//    }
//
//    @Test
//    public void hentLagringsenhetMedIdentifikator() {
//        Lagringsenhet l = tjeneste.hentLagringsenhetMedIdentifikator("boks1");
//        assertNotNull(l);
//    }
//
//    @Test
//    public void hentLagringsenheterForAvlevering() {
//        String avleveringsidentifikator = "Avlevering-1";
//        List<Lagringsenhet> lagringsenheter = tjeneste.hentLagringsenheterForAvlevering(avleveringsidentifikator);
//        assertNotNull(lagringsenheter);
//        assertEquals(3, lagringsenheter.size());
//    }
//
//    @Test
//    public void hentLagringsenheterForUkjentAvlevering() {
//        String avleveringsidentifikator = "Avlevering-ukjent";
//        List<Lagringsenhet> lagringsenheter = tjeneste.hentLagringsenheterForAvlevering(avleveringsidentifikator);
//        assertNotNull(lagringsenheter);
//        assertEquals(0, lagringsenheter.size());
//    }
}
