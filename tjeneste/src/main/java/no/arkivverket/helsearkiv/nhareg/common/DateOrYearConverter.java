package no.arkivverket.helsearkiv.nhareg.common;

import no.arkivverket.helsearkiv.nhareg.domene.common.ValidDateFormats;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DateOrYear;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateOrYearConverter implements DateOrYearConverterInterface {

    @Override
    public String fromDateOrYear(final DateOrYear dateOrYear) {
        if (dateOrYear == null) {
            return null;
        }
        
        if (dateOrYear.getDate() != null) {
            final LocalDateTime localDate = dateOrYear.getDate();
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");

            return localDate.format(formatter);
        }

        return dateOrYear.getYear().toString();
    }

    @Override
    public DateOrYear toDateOrYear(final String date) {
        if (date == null) {
            return null;
        }
        
        final DateOrYear dateOrYear = new DateOrYear();
        final LocalDate localDate = ValidDateFormats.getDate(date);
        
        if (localDate == null) {
            return null;
        }
        
        if (date.length() == 4) {
            dateOrYear.setYear(localDate.getYear());
        } else {
            dateOrYear.setDate(localDate.atStartOfDay());
        }
        
        return dateOrYear;
    }
    
}