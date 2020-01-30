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
    @XmlElement(name = "avtaleidentifikator")
    private String agreementId;

    @NotNull
    @XmlElement(name = "avtaledato")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlSchemaType(name = "date")
    @Column(name = "avtaledato")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime agreementDate;

    @Size(min = 1)
    @NotNull
    @XmlElement(name = "avtalebeskrivelse")
    @Column(name = "avtalebeskrivelse")
    private String agreementDescription;
    
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "virksomhet_organisasjonsnummer")
    @XmlElement(name = "virksomhet")
    private Business business;
    
}