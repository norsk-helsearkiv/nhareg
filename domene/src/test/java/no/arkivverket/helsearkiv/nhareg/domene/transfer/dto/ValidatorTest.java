package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.Validator;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ValidatorTest {

    @Test
    public void validator_nullObject_shouldReturnOneError() {
        final ArrayList<ValidationError> validationErrors = new Validator<>(MedicalRecordDTO.class, null).validate();
        
        assertEquals(1, validationErrors.size());
    }
    
    @Test
    public void validator_emptyDTO_shouldReturnFiveErrors() {
        final MedicalRecordDTO emptyDTO = new MedicalRecordDTO();
        final ArrayList<ValidationError> validationErrors = new Validator<>(MedicalRecordDTO.class, emptyDTO).validate();
        
        assertEquals(5, validationErrors.size());
    }
    
    @Test
    public void validator_validData_shouldReturnNoErrors() {
        final MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        final String[] storageUnits = { "1" };
        medicalRecordDTO.setStorageUnits(storageUnits);
        medicalRecordDTO.setRecordNumber("123");
        medicalRecordDTO.setSerialNumber("234");
        medicalRecordDTO.setPid("01019912345");
        medicalRecordDTO.setName("Nora");
        medicalRecordDTO.setGender("K");
        medicalRecordDTO.setBorn("1.1.1999");
        medicalRecordDTO.setDead("4.1.1999");
        medicalRecordDTO.setFirstContact("1.1.1999");
        medicalRecordDTO.setLastContact("5.1.1999");
        
        final ArrayList<ValidationError> validationErrors =
            new Validator<>(MedicalRecordDTO.class, medicalRecordDTO).validate();
        
        assert(validationErrors.isEmpty());
    }
    
}