package no.arkivverket.helsearkiv.nhareg.tjeneste;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by haraldk on 09.10.15.
 */
@RunWith(Arquillian.class)

public class KonfigparamTjenesteTest {
    @Inject
    private KonfigparamTjeneste tjeneste;

    @Test
    public void testHentingAvStringVerdi() {
        String dato = tjeneste.getVerdi("LowLim");
        assertNotNull(dato);
    }
    @Test
    public void testHentingAvDato(){
        Date date = tjeneste.getDate("LowLim");
        assertNotNull(date);
    }
    @Test
    public void testHentingAvHeltall(){
        Integer integer = tjeneste.getInt("MaxAge");
        assertEquals(Integer.valueOf(110), integer);
    }
}
