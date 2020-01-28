package no.arkivverket.helsearkiv.nhareg.domene.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.DateOrYear;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.format.DateTimeFormatter;

public class DateOrYearAdapter extends XmlAdapter<String, DateOrYear> {
    
    @Override
    public DateOrYear unmarshal(final String date) {
        final DateOrYear dateOrYear = new DateOrYear();

        if (date.length() == 4) {
            dateOrYear.setYear(Integer.parseInt(date));
        } else {
            dateOrYear.setDate(dateOrYear.getDate());
        }
        
        return dateOrYear;
    }

    @Override
    public String marshal(final DateOrYear dateOrYear) {
        if (dateOrYear == null) {
            return "";
        }
        
        if (dateOrYear.getDate() == null) {
            return String.valueOf(dateOrYear.getAsYear());
        } else {
            return dateOrYear.getDate().format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
        }
    }
    
}