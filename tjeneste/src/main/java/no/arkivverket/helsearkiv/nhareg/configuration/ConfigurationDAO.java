package no.arkivverket.helsearkiv.nhareg.configuration;

import no.arkivverket.helsearkiv.nhareg.domene.configuration.ConfigurationParameter;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Stateless
public class ConfigurationDAO {

    public static final String CONFIG_LOWLIM = "LowLim";
    public static final String CONFIG_MAXAGE = "MaxAge";
    public static final String CONFIG_WAITLIM = "WaitLim";
    public static final String CONFIG_CENTURY = "aarhundre";
    public static final String CONFIG_TEMPLATEFILE = "templateFilePath";
    public static final String CONFIG_PRINTER_PORT = "printerPort";
    public static final String CONFIG_LMR = "lmr";

    @PersistenceContext(name = "primary")
    private EntityManager entityManager;

    public String getValue(final String name) {
        final ConfigurationParameter param = entityManager.find(ConfigurationParameter.class, name);
        return param == null ? null : param.getVerdi();
    }

    public Date getDate(final String name) {
        final String value = getValue(name);
        Date date = null;

        try {
            date = new SimpleDateFormat("dd.MM.yyyy").parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public Integer getInt(final String name) {
        final String number = getValue(name);

        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        return null;
    }

    public Double getDouble(final String name) {
        final String number = getValue(name);

        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        return null;
    }
}
