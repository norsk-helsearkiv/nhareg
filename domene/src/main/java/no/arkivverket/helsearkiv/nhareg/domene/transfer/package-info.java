/**
* Schema information for generating XML.
 **/

@XmlSchema(
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns = {
        @XmlNs(prefix = "", namespaceURI = "http://www.arkivverket.no/standarder/nha/avlxml")
    }
)
@XmlJavaTypeAdapters({    
    @XmlJavaTypeAdapter(value = StringAdapter.class, type = String.class),
    @XmlJavaTypeAdapter(value = DateOrYearAdapter.class, type = DateOrYear.class),
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class, type = LocalDate.class),
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class, type = LocalDateTime.class),
})
package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.adapter.DateOrYearAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.LocalDateAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.LocalDateTimeAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.StringAdapter;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.LocalDate;
import java.time.LocalDateTime;
