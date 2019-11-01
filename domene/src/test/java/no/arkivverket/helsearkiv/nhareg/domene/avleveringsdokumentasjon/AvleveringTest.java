package no.arkivverket.helsearkiv.nhareg.domene.avleveringsdokumentasjon;

import org.junit.BeforeClass;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
    
    // Lag tester her.
}
