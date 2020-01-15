package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lagringsenhet")
public class StorageUnit implements Serializable {

    @NotNull
    @Column(unique = true, name = "identifikator")
    protected String id;

    @Id
    protected String uuid;

    @Column(name = "utskrift")
    private boolean print;

}