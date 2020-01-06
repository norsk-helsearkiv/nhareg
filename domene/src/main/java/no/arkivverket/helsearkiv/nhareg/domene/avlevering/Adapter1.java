package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Calendar;

public class Adapter1 extends XmlAdapter<String, Calendar> {

    public Calendar unmarshal(String value) {
        return (javax.xml.bind.DatatypeConverter.parseDateTime(value));
    }

    public String marshal(Calendar value) {
        if (value == null) {
            return null;
        }
        return (javax.xml.bind.DatatypeConverter.printDateTime(value));
    }

}