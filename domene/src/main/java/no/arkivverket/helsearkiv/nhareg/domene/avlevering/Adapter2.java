
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import java.util.Calendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter2 extends XmlAdapter<String, Calendar> {

    public Calendar unmarshal(String value) {
        return (javax.xml.bind.DatatypeConverter.parseDate(value));
    }

    public String marshal(Calendar value) {
        return (no.arkivverket.helsearkiv.nhareg.domene.felles.DatatypeConverter.printDate(value));
    }
}
