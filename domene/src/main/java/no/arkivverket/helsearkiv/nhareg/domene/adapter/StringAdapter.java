package no.arkivverket.helsearkiv.nhareg.domene.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringAdapter extends XmlAdapter<String, String> {
    
    @Override
    public String unmarshal(final String value) {
        return "".equals(value) ? null : value;
    }

    @Override
    public String marshal(final String value) {
        return value == null ? "" : value;
    }
}
