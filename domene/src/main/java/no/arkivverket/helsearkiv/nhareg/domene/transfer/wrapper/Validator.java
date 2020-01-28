package no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper;

import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Set;

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

    public ArrayList<ValidationError> validate() {
        return validate(obj);
    }

    public ArrayList<ValidationError> validate(T obj) {
        final ArrayList<ValidationError> validationError = new ArrayList<>();

        // Håndterer null objekt
        if (obj == null) {
            validationError.add(new ValidationError(objClass + "", "NotNull", "Object cannot be null"));
            return validationError;
        }

        //Validerer obj
        final Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        for (ConstraintViolation<T> violation : constraintViolations) {
            final String messageTemplate = violation.getConstraintDescriptor().getMessageTemplate();

            int start = messageTemplate.indexOf("constraints") + 12;
            int stop = messageTemplate.length() - 1;
            if (messageTemplate.indexOf("message") > 0) {
                stop = messageTemplate.indexOf("message") - 1;
            }

            final String attribute = violation.getPropertyPath().toString();
            final String constraint = messageTemplate.substring(start, stop);

            validationError.add((new ValidationError(attribute, constraint)));
        }

        return validationError;
    }

    public void validateWithException(T obj) {
        final ArrayList<ValidationError> validationError = validate(obj);

        if (!validationError.isEmpty()) {
            throw new ValidationErrorException(validationError);
        }
    }
}
