package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Calendar;

/**
 * <p>Java class for Agreement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Agreement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="agreementId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="avtaledato" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="avtalebeskrivelse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="virksomhet" type="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}Virksomhet"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Avtale", propOrder = {
    "agreementId",
    "avtaledato",
    "avtalebeskrivelse",
    "virksomhet"
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "avtale")
public class Agreement implements Serializable {

    @Id
    @Column(name = "avtaleidentifikator")
    @XmlElement(required = true, name = "avtaleidentifikator")
    protected String agreementId;

    @NotNull
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2.class)
    @XmlSchemaType(name = "date")
    protected Calendar avtaledato;

    @NotNull
    @XmlElement(required = true)
    protected String avtalebeskrivelse;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "virksomhet_organisasjonsnummer")
    @XmlElement(required = true)
    protected Virksomhet virksomhet;
    
}