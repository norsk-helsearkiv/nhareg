package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.domene.konfig.Konfigparam;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by haraldk on 09.10.15.
 */
@Stateless
public class KonfigparamTjeneste {

    @PersistenceContext(name = "primary")
    private EntityManager em;

    public String getVerdi(String navn){
        Konfigparam param = em.find(Konfigparam.class, navn);
        return param.getVerdi();
    }

    public Date getDate(String navn){
        String date = getVerdi(navn);
        Date d = null;
        try {
            d = new SimpleDateFormat("dd.MM.yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
    public Integer getInt(String navn){
        String tall = getVerdi(navn);
        return Integer.parseInt(tall);
    }
    public Double getDouble(String navn){
        String tall = getVerdi(navn);
        return Double.parseDouble(tall);
    }
}
