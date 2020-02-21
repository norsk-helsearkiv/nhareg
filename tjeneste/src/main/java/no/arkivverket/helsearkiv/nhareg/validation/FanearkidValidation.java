package no.arkivverket.helsearkiv.nhareg.validation;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

public class FanearkidValidation {

    public static ValidationError validate(final MedicalRecordDTO medicalRecordDTO, final Integer fieldLength) {
        final Long fanearkid = medicalRecordDTO.getFanearkid();
        
        if (fieldLength != null) {
            if (fanearkid == null) {
                return new ValidationError("fanearkid", "FeilFanearkidNull",
                                           "{fanearkidfeltlengde :" + fieldLength.toString() + "}");
            }
            if (fanearkid.toString().length() != fieldLength) {
                return new ValidationError("fanearkid", "FeilFanearkid",
                                           "{fanearkidfeltlengde :" + fieldLength.toString() + "}");
            }
        }
        
        return null;
    }
    
}