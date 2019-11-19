package no.arkivverket.helsearkiv.nhareg.tjeneste;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import javax.inject.Inject;

import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by haraldk on 09.10.15.
 *
 * Names tested must match those inserted into the database in import.sql
 */
@RunWith(Arquillian.class)
public class KonfigparamTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private KonfigparamTjeneste konfigparamTjeneste;

    @Test
    public void testHentingAvStringVerdi() {
        String dato = konfigparamTjeneste.getVerdi("LowLim");
        assertNotNull(dato);
    }

    @Test
    public void testHentingAvDato(){
        Date date = konfigparamTjeneste.getDate("LowLim");
        assertNotNull(date);
    }

    @Test
    public void testHentingAvHeltall(){
        Integer integer = konfigparamTjeneste.getInt("MaxAge");
        assertEquals(Integer.valueOf(200), integer);
    }
}
