package no.arkivverket.helsearkiv.nhareg.domene.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Gender;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class GenderAdapter extends XmlAdapter<String, Gender> {
    
    @Override
    public Gender unmarshal(final String genderCode) {
        return new Gender(genderCode, "");
    }

    @Override
    public String marshal(final Gender gender) {
        return gender.getCode();
    }
}
