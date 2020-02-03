package no.arkivverket.helsearkiv.nhareg.domene.auth;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

@Data
@Entity
@Table(name = "rolle")
public class Role {

    @Id
    @Column(name = "navn")
    @XmlElement(name = "navn")
    private String name;

}