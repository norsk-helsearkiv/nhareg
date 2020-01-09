package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Validator;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ValidatorTest {

    @Test
    public void validator_nullObject_shouldReturnOneError() {
        final ArrayList<ValidationError> validationErrors = new Validator<>(PersondataDTO.class, null).validate();
        
        assertEquals(1, validationErrors.size());
    }
    
    @Test
    public void validator_emptyDTO_shouldReturnFiveErrors() {
        final PersondataDTO emptyDTO = new PersondataDTO();
        final ArrayList<ValidationError> validationErrors = new Validator<>(PersondataDTO.class, emptyDTO).validate();
        
        assertEquals(5, validationErrors.size());
    }
    
    @Test
    public void validator_validData_shouldReturnNoErrors() {
        final PersondataDTO personalDataDTO = new PersondataDTO();
        final String[] storageUnits = { "1" };
        personalDataDTO.setStorageUnits(storageUnits);
        personalDataDTO.setRecordNumber("123");
        personalDataDTO.setSerialNumber("234");
        personalDataDTO.setPid("01019912345");
        personalDataDTO.setName("Nora");
        personalDataDTO.setGender("K");
        personalDataDTO.setBorn("1.1.1999");
        personalDataDTO.setDead("4.1.1999");
        personalDataDTO.setFirstContact("1.1.1999");
        personalDataDTO.setLastContact("5.1.1999");
        
        final ArrayList<ValidationError> validationErrors =
            new Validator<>(PersondataDTO.class, personalDataDTO).validate();
        
        assert(validationErrors.isEmpty());
    }
    
}