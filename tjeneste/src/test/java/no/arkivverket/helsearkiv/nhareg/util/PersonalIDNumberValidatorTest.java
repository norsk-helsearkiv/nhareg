package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.validation.PIDValidation;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PersonalIDNumberValidatorTest {

    @Test
    public void gyldigFnr_validFNumber_shouldBeTrue()  {
        final boolean validBirthNumber = PIDValidation.validPid("14037243710");
        assertTrue(validBirthNumber);
    }
    
    @Test
    public void gyldigFnr_validDNumber_shouldBeTrue()  {
        final boolean validDNumber = PIDValidation.validPid("64060038145");
        assertTrue(validDNumber);
    }
    
    @Test
    public void gyldigFnr_validHNumber_shouldBeTrue() {
        final boolean validHNumber = PIDValidation.validPid("19410542709");
        assertTrue(validHNumber);
    }

    @Test
    public void gyldigFnr_invalidFNumber_shouldBeFalse()  {
        final boolean invalidFNumber = PIDValidation.validPid("14037243711");
        assertFalse(invalidFNumber);
    }
    
    @Test
    public void gyldigFnr_invalidDNumber_shouldBeFalse()  {
        final boolean invalidDNumber = PIDValidation.validPid("64060038144");
        assertFalse(invalidDNumber);
    }
    
    @Test
    public void gyldigFnr_invalidHNumber_shouldBeFalse()  {
        final boolean invalidHNumber = PIDValidation.validPid("230881");
        assertFalse(invalidHNumber);
    }
    
}