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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Valideringsfeil that = (Valideringsfeil) o;

        if (attributt != null ? !attributt.equals(that.attributt) : that.attributt != null) return false;
        if (constriant != null ? !constriant.equals(that.constriant) : that.constriant != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = attributt != null ? attributt.hashCode() : 0;
        result = 31 * result + (constriant != null ? constriant.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
