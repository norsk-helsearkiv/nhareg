package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.LocalDateTimeAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateTimeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;

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
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlSchemaType(name = "date")
    @Column(name = "avtaledato")
    @Convert(converter = LocalDateTimeConverter.class)
    protected LocalDateTime agreementDate;

    @Size(min = 1)
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