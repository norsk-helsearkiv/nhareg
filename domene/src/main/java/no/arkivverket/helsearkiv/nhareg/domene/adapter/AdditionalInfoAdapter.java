package no.arkivverket.helsearkiv.nhareg.domene.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.AdditionalInfo;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdditionalInfoAdapter extends XmlAdapter<AdditionalInfo, AdditionalInfo> {

    @Override
    public AdditionalInfo unmarshal(final AdditionalInfo additionalInfo) {
        return additionalInfo;
    }

    @Override 
    public AdditionalInfo marshal(final AdditionalInfo additionalInfo) {
        return additionalInfo == null ? new AdditionalInfo() : additionalInfo;
    }
}