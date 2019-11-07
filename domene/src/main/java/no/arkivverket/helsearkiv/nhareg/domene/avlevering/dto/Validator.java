package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import java.util.ArrayList;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;

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

    public ArrayList<Valideringsfeil> valider() {
        return valider(obj);
    }

    public ArrayList<Valideringsfeil> valider(T obj) {
        ArrayList<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();

        //Håndterer null objekt
        if (obj == null) {
            valideringsfeil.add(new Valideringsfeil(objClass + "", "NotNull"));
            return valideringsfeil;
        }

        //Validerer obj
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        for (ConstraintViolation<T> feil : constraintViolations) {
            String msgTpl = feil.getConstraintDescriptor().getMessageTemplate();

            int start = msgTpl.indexOf("constraints") + 12;
            int stop = msgTpl.length() - 1;
            if (msgTpl.indexOf("message") > 0) {
                stop = msgTpl.indexOf("message") - 1;
            }

            String attributt = feil.getPropertyPath().toString();
            String constraint = msgTpl.substring(start, stop);

            valideringsfeil.add((new Valideringsfeil(attributt, constraint)));
        }
        return valideringsfeil;
    }
    public void validerMedException(T obj){
        ArrayList<Valideringsfeil> valideringsfeil = valider(obj);
        if(!valideringsfeil.isEmpty()){
            throw new ValideringsfeilException(valideringsfeil);
        }
    }
}
