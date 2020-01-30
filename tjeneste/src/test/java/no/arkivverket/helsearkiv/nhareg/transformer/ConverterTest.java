package no.arkivverket.helsearkiv.nhareg.transformer;

import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverterInterface;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DateOrYear;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Gender;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordConverter;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordConverterInterface;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static java.time.Month.MARCH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConverterTest {
    
    private MedicalRecordConverterInterface medicalRecordConverter;
    
    private DateOrYearConverterInterface dateOrYearConverter;

    @Before
    public void setUp() {
        medicalRecordConverter = new MedicalRecordConverter();
        dateOrYearConverter = new DateOrYearConverter();
    }
    
    @Test
    public void toMedicalRecordTest() {
        final MedicalRecordDTO dataDTO = new MedicalRecordDTO();
        dataDTO.setDead("2009");
        dataDTO.setPid("01010942345");
        dataDTO.setBorn("2009");
        dataDTO.setRecordNumber("123");
        dataDTO.setGender("K");
        String[] lagringsenheter = {"boks1"};
        dataDTO.setStorageUnits(lagringsenheter);
        dataDTO.setSerialNumber("234");
        dataDTO.setName("Natalie");
        dataDTO.setUuid("uuid1");
        dataDTO.setFirstContact("2009");
        dataDTO.setLastContact("2009");

        MedicalRecord medicalRecord = medicalRecordConverter.fromMedicalRecordDTO(dataDTO);

        assertEquals(dataDTO.getPid(), medicalRecord.getPid());
        final Integer year = medicalRecord.getDead().getAsYear();
        assertNotNull(year);
        assertEquals(2009, year.intValue());
    }

    @Test
    public void mapToPersonalDataDTOTest() {
        final MedicalRecordDTO medicalRecordDTO = medicalRecordConverter.toMedicalRecordDTO(getMedicalRecord());
        assertEquals(1, medicalRecordDTO.getStorageUnits().length);
        assertEquals("01010942345", medicalRecordDTO.getPid());
        assertEquals("Natalie", medicalRecordDTO.getName());
    }

    @Test
    public void toDateOrYear() {
        final DateOrYear dateOrYear = dateOrYearConverter.toDateOrYear("15.03.2015");
        assertNotNull(dateOrYear);

        final LocalDateTime date = dateOrYear.getDate();
        assertNotNull(date);
        assertEquals(15, date.getDayOfMonth());
        assertEquals(MARCH, date.getMonth());
        assertEquals(2015, date.getYear());
    }
    
    @Test
    public void validDate_toString() {
        final String dateString = "15.03.2015";
        DateOrYear dateOrYear = dateOrYearConverter.toDateOrYear(dateString);
        
        assertNotNull(dateOrYear);
        final LocalDateTime date = dateOrYear.getDate();
        assertNotNull(date);
        assertEquals(15, date.getDayOfMonth());
        assertEquals(MARCH, date.getMonth());
        assertEquals(2015, date.getYear());
        assertEquals(dateString, dateOrYearConverter.fromDateOrYear(dateOrYear));
    }

    private MedicalRecord getMedicalRecord() {
        final MedicalRecord medicalRecord = new MedicalRecord();
        final StorageUnit storageUnit = new StorageUnit();

        medicalRecord.setRecordNumber("123");
        medicalRecord.setSerialNumber("234");
        medicalRecord.setUuid("uuid1");
        medicalRecord.setPid("01010942345");
        medicalRecord.setTypePID("fodselsnummer");
        medicalRecord.setName("Natalie");
        medicalRecord.setDead(getDate());
        medicalRecord.setDeathDateUnknown(Boolean.FALSE);
        medicalRecord.setBorn(getDate());
        medicalRecord.setFirstContact(getDate());
        medicalRecord.setLastContact(getDate());

        storageUnit.setId("Boks1");
        storageUnit.setUuid("lagring-1-boks-1");
        medicalRecord.getStorageUnits().add(storageUnit);

        final Gender gender = new Gender();
        gender.setCode("K");
        gender.setDisplayName("Kvinne");
        medicalRecord.setGender(gender);

        return medicalRecord;
    }

    private DateOrYear getDate() {
        final DateOrYear date = new DateOrYear();
        date.setYear(2000);
        
        return date;
    }
    
}