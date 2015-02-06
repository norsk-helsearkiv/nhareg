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
    public void isValid_null_true() {
        boolean returnValue = validator.isValid(null, null);
        assertEquals(returnValue, true);
    }
    
    @Test
    public void isValid_skilleTegn_false() {
        boolean returnValue = validator.isValid("01-01-1999", null);
        assertEquals(returnValue, false);
    }
    
    @Test
    public void isValid_skilleTegn_true() {
        boolean returnValue = validator.isValid("01.01.1999", null);
        assertEquals(returnValue, true);
    }
    
    @Test
    public void isValid_skuddAar_false() {
        boolean returnValue = validator.isValid("29.02.2015", null);
        assertEquals(returnValue, false);
    }
    
    @Test
    public void isValid_skuddAar_true() {
        boolean returnValue = validator.isValid("29.02.2016", null);
        assertEquals(returnValue, true);
    }
    
    @Test
    public void isValid_valid_true() {
        boolean returnValue = validator.isValid("01.02.2016", null);
        assertEquals(returnValue, true);
    }
    
    @Test
    public void isValid_aar_true() {
        boolean returnValue = validator.isValid("2016", null);
        assertEquals(returnValue, true);
    }

    @Test
    public void isValid_mndAar_false() {
        boolean returnValue = validator.isValid("1.2016", null);
        assertEquals(returnValue, false);
    }
    
    @Test
    public void isValid_nuller_false() {
        boolean returnValue = validator.isValid("0.1.2016", null);
        assertEquals(returnValue, false);
    }
 
    @Test
    public void isValid_utenNull_true() {
        boolean returnValue = validator.isValid("1.1.2016", null);
        assertEquals(returnValue, true);
    }   
}
