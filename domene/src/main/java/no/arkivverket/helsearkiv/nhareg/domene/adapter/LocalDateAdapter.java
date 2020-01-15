package no.arkivverket.helsearkiv.nhareg.domene.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    
    @Override
    public LocalDate unmarshal(final String date) {
        return LocalDate.parse(date);
    }

    @Override
    public String marshal(final LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
    }
}
