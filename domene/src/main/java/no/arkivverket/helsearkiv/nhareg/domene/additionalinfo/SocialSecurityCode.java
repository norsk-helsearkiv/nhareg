package no.arkivverket.helsearkiv.nhareg.domene.additionalinfo;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.CS;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(namespace = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup-mdk")
public class SocialSecurityCode extends CS {

    @XmlTransient
    @Override
    public String getDisplayName() {
        return super.getDisplayName();
    }
    
}