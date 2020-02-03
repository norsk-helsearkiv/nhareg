package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "kjonn")
public class Gender extends CS implements Serializable {

    public Gender() {}

}