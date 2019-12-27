package no.arkivverket.helsearkiv.nhareg.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PersonalIDNumberValidatorTest {

    @Test
    public void gyldigFnr_validFNumber_shouldBeTrue()  {
        final boolean validBirthNumber = PersonnummerValiderer.gyldigFnr("14037243710");
        assertTrue(validBirthNumber);
    }
    
    @Test
    public void gyldigFnr_validDNumber_shouldBeTrue()  {
        final boolean validDNumber = PersonnummerValiderer.gyldigFnr("64060038145");
        assertTrue(validDNumber);
    }
    
    @Test
    public void gyldigFnr_validHNumber_shouldBeTrue() {
        final boolean validHNumber = PersonnummerValiderer.gyldigFnr("19410542709");
        assertTrue(validHNumber);
    }

    @Test
    public void gyldigFnr_invalidFNumber_shouldBeFalse()  {
        final boolean invalidFNumber = PersonnummerValiderer.gyldigFnr("14037243711");
        assertFalse(invalidFNumber);
    }
    
    @Test
    public void gyldigFnr_invalidDNumber_shouldBeFalse()  {
        final boolean invalidDNumber = PersonnummerValiderer.gyldigFnr("64060038144");
        assertFalse(invalidDNumber);
    }
    
    @Test
    public void gyldigFnr_invalidHNumber_shouldBeFalse()  {
        final boolean invalidHNumber = PersonnummerValiderer.gyldigFnr("230881");
        assertFalse(invalidHNumber);
    }
    
}