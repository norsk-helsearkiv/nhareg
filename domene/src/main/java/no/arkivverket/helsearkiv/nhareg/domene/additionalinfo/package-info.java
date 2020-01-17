/**
 * Schema information for generating XML.
 **/

@XmlSchema(
    namespace = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup",
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns = {
        @XmlNs(prefix = "", namespaceURI = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup"),
    })
@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(value = StringAdapter.class, type = String.class),
})
package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import no.arkivverket.helsearkiv.nhareg.domene.adapter.StringAdapter;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;