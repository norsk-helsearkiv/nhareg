package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "virksomhet")
public class Business implements Serializable {

    @Id
    @Column(name = "organisasjonsnummer")
    @XmlElement(required = true, name = "organisasjonsnummer")
    private String organizationNumber;
    
    @Size(min = 1)
    @Column(name = "navn")
    @XmlElement(required = true, name = "virksomhetsnavn")
    private String name;
    
    @Column(name = "foretaksnavn")
    @XmlElement(name = "foretaksnavn")
    private String businessName;

}