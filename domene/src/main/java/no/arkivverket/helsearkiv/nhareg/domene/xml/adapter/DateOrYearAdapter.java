package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.DateOrYear;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.format.DateTimeFormatter;

public class DateOrYearAdapter extends XmlAdapter<String, DateOrYear> {
    
    @Override
    public DateOrYear unmarshal(final String date) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String marshal(final DateOrYear dateOrYear) {
        if (dateOrYear.getDate() == null) {
            return String.valueOf(dateOrYear.getAsYear());
        } else {
            return dateOrYear.getDate().format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
        }
    }
    
}