package no.arkivverket.helsearkiv.nhareg.validation;

import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

/**
 * Created by haraldk on 31/10/2017.
 */
public class FanearkidValidation {
    
    private static final String KONFIG_FANEARKID = "fanearkid";

    public static ValidationError validate(final PersondataDTO personalDataDTO, final ConfigurationDAO configurationDAO) {
        final Integer fieldLength = configurationDAO.getInt(KONFIG_FANEARKID);
        final String fanearkid = personalDataDTO.getFanearkid();
        
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