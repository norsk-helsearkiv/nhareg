package no.arkivverket.helsearkiv.nhareg.domene.constraint;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class DateOrYearConstraintTest {
    
    private static DateOrYearValidator validator;
    
    @BeforeClass
    public static void setup() {
        validator = new DateOrYearValidator();
    }
    
    @Test
    public void validator_null_shouldReturnTrue() {
        boolean returnValue = validator.isValid(null, null);
        assertTrue(returnValue);
    }
    
    @Test
    public void validator_dateWithHyphen_shouldReturnTrue() {
        boolean returnValue = validator.isValid("01-01-1999", null);
        assertTrue(returnValue);
    }
    
    @Test
    public void validator_dateWithPeriod_shouldReturnTrue() {
        boolean returnValue = validator.isValid("01.01.1999", null);
        assertTrue(returnValue);
    }
    
    @Test
    public void validator_invalidDate_shouldReturnTrue() {
        boolean returnValue = validator.isValid("29.02.2015", null);
        assertFalse(returnValue);
    }
    
    @Test
    public void validator_validLeapYearDate_shouldReturnTrue() {
        boolean returnValue = validator.isValid("29.02.2016", null);
        assertTrue(returnValue);
    }
    
    @Test
    public void validator_validDate_shouldReturnTrue() {
        boolean returnValue = validator.isValid("01.02.2016", null);
        assertTrue(returnValue);
    }

    @Test
    public void validator_validDateWithoutLeadingZero_shouldReturnTrue() {
        boolean returnValue = validator.isValid("1.1.2016", null);
        assertTrue(returnValue);
    }

    @Test
    public void validator_validYear_shouldReturnTrue() {
        boolean returnValue = validator.isValid("2016", null);
        assertTrue(returnValue);
    }

    @Test
    public void validator_noDay_shouldReturnFalse() {
        boolean returnValue = validator.isValid("1.2016", null);
        assertFalse(returnValue);
    }
    
    @Test
    public void validator_invalidDay_shouldReturnFalse() {
        boolean returnValue = validator.isValid("0.1.2016", null);
        assertFalse(returnValue);
    }
    
}