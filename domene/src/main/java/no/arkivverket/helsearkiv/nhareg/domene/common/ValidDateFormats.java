package no.arkivverket.helsearkiv.nhareg.domene.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;

public class ValidDateFormats {

    private static final String PATTERNS =
        "[d.M.uuuu]"
            + "[uuuu-MM-dd]"
            + "[dd.MM.uuuu]"
            + "[d.MM.uuuu]"
            + "[dd.M.uuuu]"
            + "[d,M,uuuu]"
            + "[dd,MM,uuuu]"
            + "[d,MM,uuuu]"
            + "[dd,M,uuuu]"
            + "[d-M-uuuu]"
            + "[dd-MM-uuuu]"
            + "[d-MM-uuuu]"
            + "[dd-M-uuuu]"
            + "[ddMMuuuu]"
            + "[uuuu]";

    public static LocalDate getDate(final String date) {
        final DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder().parseCaseInsensitive();

        final DateTimeFormatter formatter = formatterBuilder.appendPattern(PATTERNS)
                                                            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                                                            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                                                            .toFormatter()
                                                            .withResolverStyle(ResolverStyle.STRICT);

        try {
            return formatter.parse(date, LocalDate::from);
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

}