/*
* Schema information for generating XML.
*/

@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(value = StringAdapter.class, type = String.class),
})
package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import no.arkivverket.helsearkiv.nhareg.domene.adapter.StringAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;