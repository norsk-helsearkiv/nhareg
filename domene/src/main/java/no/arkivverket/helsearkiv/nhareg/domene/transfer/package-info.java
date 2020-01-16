/**
* Schema information for generating XML.
 **/

@javax.xml.bind.annotation.XmlSchema(
    namespace = "http://www.arkivverket.no/standarder/nha/avlxml",
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns = @XmlNs(prefix = "", namespaceURI = "http://www.arkivverket.no/standarder/nha/avlxml")
)
@XmlJavaTypeAdapters({    
    @XmlJavaTypeAdapter(value = StringAdapter.class, type = String.class),
    @XmlJavaTypeAdapter(value = DateOrYearAdapter.class, type = DateOrYear.class)
})
package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.adapter.DateOrYearAdapter;
import no.arkivverket.helsearkiv.nhareg.domene.adapter.StringAdapter;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
