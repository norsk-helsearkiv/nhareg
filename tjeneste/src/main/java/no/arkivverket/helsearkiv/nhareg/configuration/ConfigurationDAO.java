package no.arkivverket.helsearkiv.nhareg.configuration;

import no.arkivverket.helsearkiv.nhareg.domene.konfig.Konfigparam;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Stateless
public class KonfigparamTjeneste {

    public static final String KONFIG_LOWLIM = "LowLim";
    public static final String KONFIG_MAXAGE = "MaxAge";
    public static final String KONFIG_WAITLIM = "WaitLim";
    public static final String KONFIG_AARHUNDRE = "aarhundre";
    public static final String KONFIG_TEMPLATEFILE = "templateFilePath";
    public static final String KONFIG_PRINTER_PORT = "printerPort";

    @PersistenceContext(name = "primary")
    private EntityManager em;

    public String getVerdi(String navn) {
        Konfigparam param = em.find(Konfigparam.class, navn);
        return param == null ? null : param.getVerdi();
    }

    public Date getDate(final String navn) {
        final String verdi = getVerdi(navn);
        Date date = null;

        try {
            date = new SimpleDateFormat("dd.MM.yyyy").parse(verdi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public Integer getInt(final String navn) {
        final String tall = getVerdi(navn);

        try {
            return Integer.parseInt(tall);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        return null;
    }

    public Double getDouble(final String navn) {
        final String tall = getVerdi(navn);

        try {
            return Double.parseDouble(tall);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        return null;
    }
}
