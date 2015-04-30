package no.arkivverket.helsearkiv.nhareg.domene.felles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by haraldk on 30.04.15.
 */
public class GyldigeDatoformater {
    public static Date getDate(String dato){
        String[] formater = {"dd.MM.yyyy",  "dd,MM,yyyy", "dd-MM-yyyy", "ddMMyyyy", "yyyy"};
        Date d = null;
        for (String format:formater){
            try{
                SimpleDateFormat df = new SimpleDateFormat(format);
                df.setLenient(false);
                d = df.parse(dato);
                return d;
            }
            catch (ParseException e){

            }
        }
        return null;
    }
}
