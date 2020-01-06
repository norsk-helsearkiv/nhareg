package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ValidationError;

/**
 * Created by haraldk on 31/10/2017.
 */
public class FanearkidValiderer {
    public static final String KONFIG_FANEARKID = "fanearkid";

    public static ValidationError valider(PersondataDTO dto, ConfigurationDAO konfig) {
        Integer fieldLength = konfig.getInt(KONFIG_FANEARKID);
        String fanearkid = dto.getFanearkid();
        if (fieldLength != null) {
            if (fanearkid == null) {
                return new ValidationError("fanearkid", "FeilFanearkidNull", "{fanearkidfeltlengde :" + fieldLength.toString() + "}");
            }
            if (fanearkid.length() != fieldLength) {
                return new ValidationError("fanearkid", "FeilFanearkid", "{fanearkidfeltlengde :" + fieldLength.toString() + "}");
            }
        }
        
        return null;
    }
}
