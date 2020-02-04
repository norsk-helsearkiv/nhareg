package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@Data
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "Enhet", propOrder = {
    "unitId",
    "unitName",
    "departmentCode",
    "reshId",
    "unitLocalCode"
})
public class Unit implements Serializable {
    
    @XmlElement(name = "enhetID")
    private String unitId;
    
    @XmlElement(name = "enhetNavn")
    private String unitName;
    
    @XmlElement(name = "offAvdKode")
    private String departmentCode;
    
    @XmlElement(name = "reshID")
    private String reshId;
    
    @XmlElement(name = "enhetLokal")
    private String unitLocalCode;
    
}