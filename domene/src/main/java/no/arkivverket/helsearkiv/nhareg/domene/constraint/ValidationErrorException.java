package no.arkivverket.helsearkiv.nhareg.domene.constraint;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

import javax.ejb.ApplicationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Exception for kasting av validationError.
 * @author arnfinns
 */
@ApplicationException(rollback = true)
public class ValidationErrorException extends ValidationException {

    private ArrayList<ValidationError> validationError = new ArrayList<ValidationError>();
    
    public ValidationErrorException(final Collection<ValidationError> validationError) {
        super();
        this.validationError.addAll(validationError);
        this.validationError.size();
    }

    public ArrayList<ValidationError> getValidationError() {
        return validationError;
    }

    public void setValidationError(ArrayList<ValidationError> validationError) {
        this.validationError = validationError;
    }
    
}