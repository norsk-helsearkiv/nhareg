package no.arkivverket.helsearkiv.nhareg.domene.auth;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Rolle {

    @Id
    private String navn;

}