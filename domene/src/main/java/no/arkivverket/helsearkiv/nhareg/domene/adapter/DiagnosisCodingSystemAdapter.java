package no.arkivverket.helsearkiv.nhareg.domene.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DiagnosisCodingSystemAdapter extends XmlAdapter<String, String> {

    @Override
    public String unmarshal(final String value) {
        return value;
    }

    @Override
    public String marshal(final String value) {
        if (value == null || value.isEmpty()) {
            return "0";
        }

        return value;
    }
    
}