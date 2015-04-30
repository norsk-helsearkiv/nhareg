package no.arkivverket.helsearkiv.nhareg.domene.constraints;

import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author robing
 */
public class DagEllerAarValidator implements ConstraintValidator<DagEllerAar, String> {
    
    public void initialize(DagEllerAar constraintAnnotation) {
    }

    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (object == null || object.isEmpty() || object.toLowerCase().equals("mors")) {
            return true;
        }

        /*
            Godkjente objekt verdier:
            - DD.MM.AAAA (01.01.1999)
            - D.M.AAAA (1.1.1999)
            - AAAA (1999)
        */
        return GyldigeDatoformater.getDate(object)!=null;
        //return object.matches("^\\d{4}|^(?:(?:31(\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");
    }
}