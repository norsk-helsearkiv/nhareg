package no.arkivverket.helsearkiv.nhareg.domene.avleveringsdokumentasjon;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author robing
 */
public class AvleveringTest {
    
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    public void testPasientjournalGrunnopplysningerIsNull() {

        Pasientjournal pasientjournal = new Pasientjournal();

        Set<ConstraintViolation<Pasientjournal>> constraintViolations =
            validator.validate(pasientjournal);
        
        assertTrue(constraintViolations.size() > 0);
        assertEquals(
            "may not be null", constraintViolations.iterator().next().getMessage());
    }
}
