
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;

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
 * <p>Java class for DatoEllerAar complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DatoEllerAar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="dato" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="aar">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="1800"/>
 *               &lt;maxInclusive value="2099"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatoEllerAar", propOrder = {
    "dato",
    "aar"
})
@Data
public class DatoEllerAar implements Serializable {

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar dato;
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
