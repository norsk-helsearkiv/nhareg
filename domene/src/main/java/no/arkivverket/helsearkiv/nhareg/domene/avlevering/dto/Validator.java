package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValidationErrorException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author robing
 * @param <T> Objektklassen som skal valideres
 */
public class Validator<T> {

    private final Class<T> objClass;
    private T obj = null;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final javax.validation.Validator validator = factory.getValidator();

    public Validator(Class<T> objClass) {
        this.objClass = objClass;
    }

    public Validator(Class<T> objClass, T obj) {
        this(objClass);
        this.obj = obj;
    }

    public ArrayList<ValidationError> valider() {
        return valider(obj);
    }

    public ArrayList<ValidationError> valider(T obj) {
        ArrayList<ValidationError> validationError = new ArrayList<ValidationError>();

        // Håndterer null objekt
        if (obj == null) {
            validationError.add(new ValidationError(objClass + "", "NotNull", "Object cannot be null"));
            return validationError;
        }

        //Validerer obj
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        for (ConstraintViolation<T> feil : constraintViolations) {
            String messageTemplate = feil.getConstraintDescriptor().getMessageTemplate();

            int start = messageTemplate.indexOf("constraints") + 12;
            int stop = messageTemplate.length() - 1;
            if (messageTemplate.indexOf("message") > 0) {
                stop = messageTemplate.indexOf("message") - 1;
            }

            String attributt = feil.getPropertyPath().toString();
            String constraint = messageTemplate.substring(start, stop);

            validationError.add((new ValidationError(attributt, constraint, feil.getMessage())));
        }

        return validationError;
    }

    public void validerMedException(T obj) {
        ArrayList<ValidationError> validationError = valider(obj);

        if (!validationError.isEmpty()) {
            throw new ValidationErrorException(validationError);
        }
    }
}
