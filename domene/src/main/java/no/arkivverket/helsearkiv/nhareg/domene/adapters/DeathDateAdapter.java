package no.arkivverket.helsearkiv.nhareg.domene.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DeathDateAdapter extends XmlAdapter<Integer, Boolean> {

    @Override
    public Boolean unmarshal(final Integer value) {
        return value.equals(0);
    }

    @Override
    public Integer marshal(final Boolean value) {
        // NOTE this switches T/F, 0 for true and 1 for false as it's the opposite in the XML representation
        return value ? 0 : 1;
    }

}