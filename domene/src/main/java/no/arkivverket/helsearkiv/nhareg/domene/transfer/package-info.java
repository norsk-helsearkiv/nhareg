/**
* Schema information for generating XML.
 **/

@XmlSchema(
    elementFormDefault = XmlNsForm.QUALIFIED
)
@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(value = DateOrYearAdapter.class, type = DateOrYear.class),
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class, type = LocalDate.class),
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class, type = LocalDateTime.class),
})
package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.DateOrYearAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.LocalDateAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.xml.adapter.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.LocalDate;
import java.time.LocalDateTime;
