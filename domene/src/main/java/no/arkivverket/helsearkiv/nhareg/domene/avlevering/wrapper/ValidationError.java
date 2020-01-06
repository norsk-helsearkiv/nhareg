package no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 *  ValidationError med attributt og constraint som feiler.
 * @author robing
 */
@Data
@AllArgsConstructor
public class ValidationError implements Serializable {
    
    private String attribute;
    
    private String constraint;
    
    private String message;

    public ValidationError(final String attribute, final String constraint) {
        setAttribute(attribute);
        setConstraint(constraint);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        final ValidationError that = (ValidationError) obj;

        if (!Objects.equals(attribute, that.attribute)) return false;
        if (!Objects.equals(constraint, that.constraint)) return false;
        
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        int result = attribute != null ? attribute.hashCode() : 0;
        result = 31 * result + (constraint != null ? constraint.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        
        return result;
    }
    
}