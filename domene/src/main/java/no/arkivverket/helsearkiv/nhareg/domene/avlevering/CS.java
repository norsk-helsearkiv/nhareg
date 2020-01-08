package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * 
 * Denne klassen benyttes for å representere datatypen CS.
 * Benyttes for registrering av kodet verdi hvor koden angis i form av en tekststreng og med mulighet til å angi
 * kodemeningen som opsjon. Kodeverket og versjonen av dette skal være entydig bestemt av dataelementtypen.
 * 
 * <p>Java class for CS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CS", propOrder = {
    "code",
    "displayName"
})
@XmlSeeAlso({
    Gender.class
})
@Data
@Entity
@Table(name = "cs")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class CS implements Serializable {

    @Id
    @NotNull
    @XmlElement(required = true)
    protected String code;
    
    @NotNull
    @XmlElement(required = true)
    protected String displayName;

}