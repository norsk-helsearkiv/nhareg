package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 
 * Denne klassen benyttes for å representere datatypen CS.
 * Benyttes for registrering av kodet verdi hvor koden angis i form av en tekststreng og med mulighet til å angi
 * kodemeningen som opsjon. Kodeverket og versjonen av dette skal være entydig bestemt av dataelementtypen.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CS", propOrder = {
    "code",
    "displayName"
})
// @XmlSeeAlso({
//     Gender.class,
//     SocialSecurityCode.class
// })
@Data
@NoArgsConstructor
@AllArgsConstructor
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