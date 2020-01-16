package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "arkivskaper")
@XmlType(name = "arkivskaper", propOrder = {
    "uuid", 
    "code",
    "name",
    "description"
})
public class ArchiveCreator implements Serializable {
    
    @Id
    private String uuid;
    
    @Column(name = "kode", unique = true, nullable = false)
    @XmlElement(name = "arkivskaperkode")
    private String code;
    
    @Column(name = "navn")
    @XmlElement(name = "arkivskaper")
    private String name;
    
    @Column(name = "beskrivelse")
    @XmlElement(name = "arkivskaperbeskrivelse")
    private String description;
    
}