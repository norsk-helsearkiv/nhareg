package no.arkivverket.helsearkiv.nhareg.domene.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Calendar;

import static javax.xml.bind.DatatypeConverter.parseDateTime;
import static javax.xml.bind.DatatypeConverter.printDateTime;

public class StringDateTimeAdapter extends XmlAdapter<String, Calendar> {

    public Calendar unmarshal(final String value) {
        return (parseDateTime(value));
    }

    public String marshal(final Calendar value) {
        if (value == null) {
            return null;
        }
        
        return (printDateTime(value));
    }

}