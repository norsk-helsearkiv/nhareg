package no.arkivverket.helsearkiv.nhareg.util;

import java.util.Calendar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arnfinns
 */
public class StringTilDatoEllerAarTransformerTest {

    StringTilDatoEllerAarTransformer instance = new StringTilDatoEllerAarTransformer();

    public StringTilDatoEllerAarTransformerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of transform method, of class StringTilDatoEllerAarTransformer.
     */
    @Test
    public void testTransformAar() {
        String tid = "1998";
        DatoEllerAar result = instance.transform(tid);
        assertNotNull(result);
        assertEquals(1998, result.getAar().intValue());
        assertNull(result.getDato());
    }

    @Test
    public void testTransformDato() {
        String tid = "02.03.2015";
        DatoEllerAar result = instance.transform(tid);
        assertNotNull(result);
        assertEquals(2, result.getDato().get(Calendar.DAY_OF_MONTH));
        assertEquals(2, result.getDato().get(Calendar.MONTH));
        assertEquals(2015, result.getDato().get(Calendar.YEAR));
        assertNull(result.getAar());
    }
    
    @Test
    public void testTransformDatoUtenledendeNuller() {
        String tid = "2.3.2015";
        DatoEllerAar result = instance.transform(tid);
        assertNotNull(result);
        assertEquals(2, result.getDato().get(Calendar.DAY_OF_MONTH));
        assertEquals(2, result.getDato().get(Calendar.MONTH));
        assertEquals(2015, result.getDato().get(Calendar.YEAR));
        assertNull(result.getAar());
    }
    
    @Test
    public void testTransformNull() {
        String tid = null;
        DatoEllerAar result = instance.transform(tid);
        assertNull(result);
    }

}
