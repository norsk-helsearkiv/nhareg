package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by haraldk on 25/10/2017.
 */
public class FlyttPasientjournalDTO implements Serializable {

    private List<String> pasientjournalUuids;
    private String lagringsenhetIdentifikator;

    public List<String> getPasientjournalUuids() {
        return pasientjournalUuids;
    }

    public void setPasientjournalUuids(List<String> pasientjournalUuids) {
        this.pasientjournalUuids = pasientjournalUuids;
    }

    public String getLagringsenhetIdentifikator() {
        return lagringsenhetIdentifikator;
    }

    public void setLagringsenhetIdentifikator(String lagringsenhetIdentifikator) {
        this.lagringsenhetIdentifikator = lagringsenhetIdentifikator;
    }
}
