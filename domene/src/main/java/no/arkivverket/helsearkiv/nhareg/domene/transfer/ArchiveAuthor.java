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
@XmlType(name = "",
    propOrder = {
        "code",
        "name",
        "description"
    })
@XmlAccessorType(value = XmlAccessType.FIELD)
@Entity
@Table(name = "arkivskaper")
public class ArchiveAuthor implements Serializable {

    @XmlTransient
    @Id
    @Column(name = "uuid")
    private String uuid;

    @XmlElement(name = "kode")
    @Column(name = "kode", unique = true)
    private String code;

    @XmlElement(name = "navn")
    @Column(name = "navn", unique = true, nullable = false)
    private String name;

    @XmlElement(name = "beskrivelse")
    @Column(name = "beskrivelse")
    private String description;

}