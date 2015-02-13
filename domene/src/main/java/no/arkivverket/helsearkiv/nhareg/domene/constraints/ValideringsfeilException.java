package no.arkivverket.helsearkiv.nhareg.domene.constraints;

import java.util.ArrayList;
import java.util.Collection;
import javax.validation.ValidationException;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import javax.ejb.ApplicationException;

/**
 * Exception for kasting av valideringsfeil.
 * @author arnfinns
 */
@ApplicationException(rollback = true)
public class ValideringsfeilException extends ValidationException {

    private ArrayList<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();
    public ValideringsfeilException(Collection<Valideringsfeil> valideringsfeil){
        super();
        this.valideringsfeil.addAll(valideringsfeil);
        this.valideringsfeil.size();
    }

    public ArrayList<Valideringsfeil> getValideringsfeil() {
        return valideringsfeil;
    }

    public void setValideringsfeil(ArrayList<Valideringsfeil> valideringsfeil) {
        this.valideringsfeil = valideringsfeil;
    }
    
}
