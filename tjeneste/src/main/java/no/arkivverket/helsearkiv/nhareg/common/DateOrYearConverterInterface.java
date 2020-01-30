package no.arkivverket.helsearkiv.nhareg.common;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.DateOrYear;

public interface DateOrYearConverterInterface {

    String fromDateOrYear(final DateOrYear dateOrYear);
    
    DateOrYear toDateOrYear(final String date);
    
}