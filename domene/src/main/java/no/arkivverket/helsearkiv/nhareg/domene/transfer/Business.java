package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Virksomhet", propOrder = {
    "organizationNumber",
    "name",
    "businessName"
})
@Data
@Entity
@Table(name = "virksomhet")
public class Business implements Serializable {

    @Id
    @Column(name = "organisasjonsnummer")
    @XmlElement(required = true)
    protected String organizationNumber;
    
    @Column(name = "navn")
    @XmlElement(required = true, name = "navn")
    protected String name;
    
    @Column(name = "foretaksnavn")
    @XmlElement(name = "foretaksnavn")
    protected String businessName;

}