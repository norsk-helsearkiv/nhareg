//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.27 at 12:13:25 PM CET 
//


package no.arkivverket.helsearkiv.nhareg.domene.avleveringsdokumentasjon;

import java.util.Calendar;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter2
    extends XmlAdapter<String, Calendar>
{


    public Calendar unmarshal(String value) {
        return (javax.xml.bind.DatatypeConverter.parseDate(value));
    }

    public String marshal(Calendar value) {
        return (no.arkivverket.helsearkiv.nhareg.domene.felles.DatatypeConverter.printDate(value));
    }

}
