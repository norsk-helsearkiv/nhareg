package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DeathDateKnownAdapter extends XmlAdapter<Integer, Boolean> {

    @Override
    public Boolean unmarshal(final Integer value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer marshal(final Boolean value) { 
        return 1;
    }

}