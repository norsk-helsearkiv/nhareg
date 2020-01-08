package no.arkivverket.helsearkiv.nhareg.domene.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "rolle")
public class Role {

    @Id
    @Column(name = "navn")
    @JsonProperty(value = "navn")
    private String name;

}