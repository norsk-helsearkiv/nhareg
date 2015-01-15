package no.arkivverket.helsearkiv.nhareg.domene.avleveringsdokumentasjon;

import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class AvleveringsbeskrivelseTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testManufacturerIsNull() {

        Avleveringsbeskrivelse avleveringsbeskrivelse = new Avleveringsbeskrivelse();

        Set<ConstraintViolation<Avleveringsbeskrivelse>> constraintViolations =
            validator.validate(avleveringsbeskrivelse);

        assertEquals(1, constraintViolations.size());
        assertEquals(
            "may not be null", constraintViolations.iterator().next().getMessage());

    }

    @Test
    public void testLicensePlateTooShort() {

        Avleveringsbeskrivelse avleveringsbeskrivelse = new Avleveringsbeskrivelse();

        Set<ConstraintViolation<Avleveringsbeskrivelse>> constraintViolations = 
            validator.validate(avleveringsbeskrivelse);

        assertEquals(1, constraintViolations.size());
        assertEquals(
            "may not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testAvleveringsbeskrivelseIsValid() {

        Avleveringsbeskrivelse avleveringsbeskrivelse = new Avleveringsbeskrivelse();

        Set<ConstraintViolation<Avleveringsbeskrivelse>> constraintViolations =
            validator.validate(avleveringsbeskrivelse);

        assertEquals(1, constraintViolations.size());
               assertEquals(
            "may not be null", constraintViolations.iterator().next().getMessage());
    }
}