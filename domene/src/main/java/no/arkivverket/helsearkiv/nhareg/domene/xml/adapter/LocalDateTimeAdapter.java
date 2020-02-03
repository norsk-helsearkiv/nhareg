package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(final String date) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String marshal(final LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
    }
    
}