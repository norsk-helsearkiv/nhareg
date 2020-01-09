package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StorageUnit", propOrder = {
    "id"
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lagringsenhet")
public class StorageUnit implements Serializable {

    @NotNull
    @XmlElement(required = true)
    @Column(unique = true, name = "identifikator")
    protected String id;

    @Id
    @XmlAttribute(name = "uuid")
    protected String uuid;

    @XmlTransient
    @Column(name = "utskrift")
    private boolean print;

}