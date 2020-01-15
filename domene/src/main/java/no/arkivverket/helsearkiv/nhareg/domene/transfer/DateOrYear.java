package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.LocalDateAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateTimeConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Type for å representere en dato eller et årstall
 */
@Data
@NoArgsConstructor
@Embeddable
public class DateOrYear implements Serializable {

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlSchemaType(name = "date")
    @Column(name = "dato")
    @Convert(converter = LocalDateTimeConverter.class)
    protected LocalDateTime date;

    @Min(value = 1800)
    @Max(value = 2099)
    @Column(name = "aar")
    protected Integer year;

    /**
     * Transforms the value to string.
     * @return The string value of either the date or year.
     */
    public String toString() {
        if (date != null) {
            return date.toString();
        } else if (year != null) {
            return year.toString();
        }
        
        return null;
    }

    /**
     * Get the year either from date or aar.
     * @return int value of the year.
     */
    public int getAsYear() {
        if (date != null) {
            return date.getYear();
        }
        
        return year;
    }
    
}
