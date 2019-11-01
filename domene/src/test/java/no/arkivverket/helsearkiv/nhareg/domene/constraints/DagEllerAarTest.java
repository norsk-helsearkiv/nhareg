package no.arkivverket.helsearkiv.nhareg.domene.constraints;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.junit.runners.JUnit4;

/**
 *
 * @author robing
 */
@RunWith(JUnit4.class)
public class DagEllerAarTest {
    
    private static DagEllerAarValidator validator;
    
    @BeforeClass
    public static void setup() {
        validator = new DagEllerAarValidator();
    }
    
    @Test
    public void dagEllerAarValidator_null_skalGiTrue() {
        boolean returnValue = validator.isValid(null, null);
        assertTrue(returnValue);
    }
    
    @Test
    public void dagEllerAarValidator_bindestrek_skalGiTrue() {
        boolean returnValue = validator.isValid("01-01-1999", null);
        assertTrue(returnValue);
    }
    
    @Test
    public void dagEllerAarValidator_punktum_skalGiTrue() {
        boolean returnValue = validator.isValid("01.01.1999", null);
        assertTrue(returnValue);
    }
    
    @Test
    public void dagEllerAarValidator_ikkeEksisterendeDato_skalGiFalse() {
        boolean returnValue = validator.isValid("29.02.2015", null);
        assertFalse(returnValue);
    }
    
    @Test
    public void dagEllerAarValidator_gyldigSkuddaarsDato_skalGiTrue() {
        boolean returnValue = validator.isValid("29.02.2016", null);
        assertTrue(returnValue);
    }
    
    @Test
    public void dagEllerAarValidator_gyldigDatoFormatEn_skalGiTrue() {
        boolean returnValue = validator.isValid("01.02.2016", null);
        assertTrue(returnValue);
    }

    @Test
    public void dagEllerAarValidator_gyldigDatoFormatTo_skalGiTrue() {
        boolean returnValue = validator.isValid("1.1.2016", null);
        assertTrue(returnValue);
    }

    @Test
    public void dagEllerAarValidator_gyldigAar_skalGiTrue() {
        boolean returnValue = validator.isValid("2016", null);
        assertTrue(returnValue);
    }

    @Test
    public void dagEllerAarValidator_ugyldigMaanedAar_skalGiFalse() {
        boolean returnValue = validator.isValid("1.2016", null);
        assertFalse(returnValue);
    }
    
    @Test
    public void dagEllerAarValidator_ugyldigDato_skalGiFalse() {
        boolean returnValue = validator.isValid("0.1.2016", null);
        assertFalse(returnValue);
    }
}
