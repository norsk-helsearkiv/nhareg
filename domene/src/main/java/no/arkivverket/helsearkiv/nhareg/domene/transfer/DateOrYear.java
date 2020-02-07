package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateTimeConverter;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.DateOrYearAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.LocalDateTimeAdapter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents either a year as int or a date as a LocalDate.
 */
@Data
@NoArgsConstructor
@Embeddable
@XmlJavaTypeAdapter(value = DateOrYearAdapter.class, type = DateOrYear.class)
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DateOrYear implements Serializable {

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlSchemaType(name = "date")
    @Column(name = "dato")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime date;

    @Min(value = 1800)
    @Max(value = 2099)
    @Column(name = "aar")
    private Integer year;

    /**
     * Transforms the value to string.
     * @return The string value of either the date or year.
     */
    public String toString() {
        if (date != null) {
            return date.format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
        } else if (year != null) {
            return year.toString();
        }
        
        return null;
    }

    /**
     * Get the year either from date or aar.
     * @return int value of the year.
     */
    public Integer getAsYear() {
        if (date != null) {
            return date.getYear();
        }
        
        return year;
    }
    
}
