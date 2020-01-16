package no.arkivverket.helsearkiv.nhareg.domene.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static javax.xml.bind.DatatypeConverter.parseDate;

public class StringDateAdapter extends XmlAdapter<String, Calendar> {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    
    public Calendar unmarshal(final String value) {
        return (parseDate(value));
    }

    public String marshal(final Calendar value) {
        if (value != null) {
            return format.format(value.getTime());
        } else {
            return null;
        }
    }

}