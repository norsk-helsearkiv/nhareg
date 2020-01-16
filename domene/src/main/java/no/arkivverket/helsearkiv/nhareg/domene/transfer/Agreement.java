package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.StringDateAdapter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Calendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "avtale", propOrder = {
    "agreementId",
    "agreementDate",
    "agreementDescription",
    "business"
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "avtale")
public class Agreement implements Serializable {

    @Id
    @Column(name = "avtaleidentifikator")
    @XmlElement(required = true, name = "avtaleidentifikator")
    protected String agreementId;

    @NotNull
    @XmlElement(required = true, type = String.class, name = "avtaledato")
    @XmlJavaTypeAdapter(StringDateAdapter.class)
    @XmlSchemaType(name = "date")
    @Column(name = "avtaledato")
    protected Calendar agreementDate;

    @NotNull
    @XmlElement(required = true, name = "avtalebeskrivelse")
    @Column(name = "avtalebeskrivelse")
    protected String agreementDescription;
    
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "virksomhet_organisasjonsnummer")
    @XmlElement(required = true, name = "virksomhet")
    protected Business business;
    
}