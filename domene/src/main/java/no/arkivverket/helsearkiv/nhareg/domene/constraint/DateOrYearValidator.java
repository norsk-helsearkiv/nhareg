package no.arkivverket.helsearkiv.nhareg.domene.constraint;

import no.arkivverket.helsearkiv.nhareg.domene.common.ValidDateFormats;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateOrYearValidator implements ConstraintValidator<DateOrYearConstraint, String> {
    
    public void initialize(DateOrYearConstraint constraintAnnotation) {
    }

    public boolean isValid(final String object, final ConstraintValidatorContext constraintContext) {
        if (object == null || 
            object.isEmpty() ||
            object.toLowerCase().equals("mors") || 
            object.toLowerCase().equals("m") ||
            object.toLowerCase().equals("ukjent") || 
            object.toLowerCase().equals("u")) {
            return true;
        }

        return ValidDateFormats.getDate(object) != null;
        //return object.matches("^\\d{4}|^(?:(?:31(\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");
    }
}