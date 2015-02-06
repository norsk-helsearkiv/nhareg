package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import java.util.ArrayList;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;

/**
 *
 * @author robing
 * @param <T> Objektklassen som skal valideres
 */
public class Validator<T> {
    
    private final Class<T> objClass;
    private final T obj;
    private final javax.validation.Validator validator;
    
    public Validator(Class<T> objClass, T obj) {
        this.objClass = objClass;
        this.obj = obj;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    public ArrayList<Valideringsfeil> valider() {
        ArrayList<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();
        
        //Håndterer null objekt
        if(obj == null) {
            valideringsfeil.add(new Valideringsfeil(objClass + "", "NotNull"));
            return valideringsfeil;
        }
        
        //Validerer obj
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        for(ConstraintViolation<T> feil : constraintViolations) {
            String msgTpl = feil.getConstraintDescriptor().getMessageTemplate();
            
            int start = msgTpl.indexOf("constraints") + 12;
            int stop = msgTpl.length() - 1;
            if(msgTpl.indexOf("message") > 0) {
                stop = msgTpl.indexOf("message") - 1;
            }

            String attributt = feil.getPropertyPath().toString();
            String constraint = msgTpl.substring(start, stop);

            valideringsfeil.add((new Valideringsfeil(attributt, constraint)));
        }
        return valideringsfeil;
    }
    
}
