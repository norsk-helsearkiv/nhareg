package no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper;

import java.io.Serializable;

/**
 *  Valideringsfeil med attributt og constraint som feiler.
 * @author robing
 */
public class Valideringsfeil implements Serializable {
    
    private String attributt;
    private String constriant;

        private String message;

    public Valideringsfeil(String attributt, String constriant) {
        setAttributt(attributt);
        setConstriant(constriant);
    }
    public Valideringsfeil(String attributt, String constriant, String message) {
        setAttributt(attributt);
        setConstriant(constriant);
        setMessage(message);
    }
    public String getAttributt() {
        return attributt;
    }

    public void setAttributt(String attributt) {
        this.attributt = attributt;
    }

    public String getConstriant() {
        return constriant;
    }

    public void setConstriant(String constriant) {
        this.constriant = constriant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
