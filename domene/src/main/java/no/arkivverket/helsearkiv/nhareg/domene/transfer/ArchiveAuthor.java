package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "arkivskaper")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "arkivskaper", propOrder = {
    "uuid", 
    "code",
    "name",
    "description"
})
public class ArchiveAuthor implements Serializable {
    
    @Id
    @XmlTransient
    private String uuid;

    @Column(name = "kode", unique = true)
    @XmlElement(name = "arkivskaperkode")
    private String code;
    
    @Column(name = "navn", unique = true, nullable = false)
    @XmlElement(name = "arkivskaper")
    private String name;
    
    @Column(name = "beskrivelse")
    @XmlElement(name = "arkivskaperbeskrivelse")
    private String description;
    
}