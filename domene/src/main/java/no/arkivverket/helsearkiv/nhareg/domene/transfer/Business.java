package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "virksomhet", propOrder = {
    "name",
    "businessName",
    "organizationNumber"
})
@Data
@Entity
@Table(name = "virksomhet")
public class Business implements Serializable {

    @Id
    @Column(name = "organisasjonsnummer")
    @XmlElement(required = true, name = "organisasjonsnummer")
    protected String organizationNumber;
    
    @JsonProperty(value = "navn")
    @Size(min = 1)
    @Column(name = "navn")
    @XmlElement(required = true, name = "virksomhetsnavn")
    protected String name;
    
    @Column(name = "foretaksnavn")
    @XmlElement(name = "foretaksnavn", nillable = true)
    protected String businessName;

}