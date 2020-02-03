package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.Contact;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;

public class ContactAdapter extends XmlAdapter<Set<Contact>, Set<Contact>> {

    @Override 
    public Set<Contact> unmarshal(final Set<Contact> v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Contact> marshal(final Set<Contact> value) {
        if (value.isEmpty()) {
            value.add(new Contact());
        }
        
        return value;
    }
}