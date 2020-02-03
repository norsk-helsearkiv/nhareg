package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlType(name = "",
    propOrder = {
        "xmlns",
        "id"
    })
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Identifier {

    @JacksonXmlProperty(isAttribute = true)
    @XmlAttribute
    private String xmlns = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup";

    @JacksonXmlText
    private String id;

}