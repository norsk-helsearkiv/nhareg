package no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper;

import java.io.Serializable;

/**
 *  Valideringsfeil med attributt og constraint som feiler.
 * @author robing
 */
public class Valideringsfeil implements Serializable {
    
    private String attribute;
    private String constraint;
    private String message;

    public Valideringsfeil(String attribute, String constraint) {
        setAttribute(attribute);
        setConstraint(constraint);
    }
    
    public Valideringsfeil(String attribute, String constraint, String message) {
        setAttribute(attribute);
        setConstraint(constraint);
        setMessage(message);
    }
    
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
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

        if (attribute != null ? !attribute.equals(that.attribute) : that.attribute != null) return false;
        if (constraint != null ? !constraint.equals(that.constraint) : that.constraint != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = attribute != null ? attribute.hashCode() : 0;
        result = 31 * result + (constraint != null ? constraint.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
