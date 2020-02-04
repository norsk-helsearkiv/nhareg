package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DeathDateKnownAdapter extends XmlAdapter<Integer, Boolean> {

    @Override
    public Boolean unmarshal(final Integer value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer marshal(final Boolean value) {
        // NOTE this switches T/F, 0 for true and 1 for false as it's the opposite in the XML representation
        return value ? 0 : 1;
    }

}