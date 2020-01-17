package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true) 
@XmlType(name = "kontakt", propOrder = {
    "services",
    "contactType",
    "activityLocation",
    "activityType"
})
public class Contact extends Episode implements Serializable {
    
    @XmlElement(name = "tjeneste")
    private List<Service> services;
    
    @NotNull
    @XmlElement(name = "kontakttype")
    private ContactType contactType;
    
    @XmlElement(name = "stedAktivitet")
    private ActivityLocation activityLocation;
    
    @XmlElement(name = "polkonAktivitet")
    private ActivityType activityType;
    
}