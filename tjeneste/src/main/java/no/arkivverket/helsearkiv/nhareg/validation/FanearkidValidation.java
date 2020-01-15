package no.arkivverket.helsearkiv.nhareg.validation;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.PersonalDataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

public class FanearkidValidation {

    public static ValidationError validate(final PersonalDataDTO personalDataDTO, final Integer fieldLength) {
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