package no.arkivverket.helsearkiv.nhareg.domene.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    
    @XmlElement(name = "navn")
    private String name;

}