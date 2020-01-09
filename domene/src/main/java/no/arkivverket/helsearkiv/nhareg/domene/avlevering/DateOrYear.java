package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.adapters.StringDateAdapter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * Type for å representere en dato eller et årstall
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatoEllerAar", propOrder = {
    "dato",
    "aar"
})
@Data
@Embeddable
public class DateOrYear implements Serializable {

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(StringDateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Calendar dato;

    @Min(value = 1800)
    @Max(value = 2099)
    protected Integer aar;

    /**
     * Transforms the value to string.
     * @return The string value of either the date or year.
     */
    public String toString() {
        return this.dato != null ? this.dato.toString() : this.aar.toString();
    }

    /**
     * Get the year either from date or aar.
     * @return int value of the year.
     */
    public int getYear() {
        if (dato != null) {
            return dato.get(Calendar.YEAR);
        }
        
        return aar;
    }
    
    public String getStringValue() {
        if (dato != null) {
            final LocalDate localDate = LocalDateTime.ofInstant(dato.toInstant(), ZoneId.systemDefault()).toLocalDate();
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return localDate.format(formatter);
        }
        return aar.toString();
    }
}
