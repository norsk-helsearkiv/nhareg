package no.arkivverket.helsearkiv.nhareg.transformer;

import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverterInterface;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.*;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.PersonalDataDTO;
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
    }
    
    @Test
    public void toMedicalRecordTest() {
        final PersonalDataDTO dataDTO = new PersonalDataDTO();
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

        MedicalRecord medicalRecord = medicalRecordConverter.fromPersonalDataDTO(dataDTO);

        assertEquals(dataDTO.getPid(), medicalRecord.getBaseProperties().getIdentifikator().getPid());
        final int year = medicalRecord.getBaseProperties().getDead().getAsYear();
        assertEquals(2009, year);
    }

    @Test
    public void mapToPersonalDataDTOTest() {
        final PersonalDataDTO personalDataDTO = medicalRecordConverter.toPersonalDataDTO(getMedicalRecord());
        assertEquals(1, personalDataDTO.getStorageUnits().length);
        assertEquals("01010942345", personalDataDTO.getPid());
        assertEquals("Natalie", personalDataDTO.getName());
    }

    @Test
    public void toDateOrYear() {
        final DateOrYear dateOrYear = DateOrYearConverter.toDateOrYear("15.03.2015");
        assertNotNull(dateOrYear);
        assertNotNull(dateOrYear.getDato());
        assertEquals(15, dateOrYear.getDato().get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.MARCH, dateOrYear.getDato().get(Calendar.MONTH));
        assertEquals(2015, dateOrYear.getDato().get(Calendar.YEAR));
    }
    
    @Test
    public void validDate_toString() {
        final String datoString = "15.03.2015";
        DateOrYear dateOrYear = DateOrYearConverter.toDateOrYear(datoString);
        assertNotNull(dateOrYear);
        assertNotNull(dateOrYear.getDato());
        assertEquals(15, dateOrYear.getDato().get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.MARCH, dateOrYear.getDato().get(Calendar.MONTH));
        assertEquals(2015, dateOrYear.getDato().get(Calendar.YEAR));
        //
        assertEquals(datoString, dateOrYear.getStringValue());
    }

    private MedicalRecord getMedicalRecord() {
        final MedicalRecord medicalRecord = new MedicalRecord();
        final StorageUnit storageUnit = new StorageUnit();
        final BaseProperties baseProperties = new BaseProperties();

        medicalRecord.setRecordNumber("123");
        medicalRecord.setSerialNumber("234");
        medicalRecord.setUuid("uuid1");
        
        storageUnit.setId("Boks1");
        storageUnit.setUuid("lagring-1-boks-1");
        medicalRecord.getStorageUnit().add(storageUnit);

        Identifikator id = new Identifikator();
        id.setPid("01010942345");
        id.setTypePID("fodselsnummer");
        baseProperties.setIdentifikator(id);

        baseProperties.setName("Natalie");
        baseProperties.setDead(getDate());
        baseProperties.setDeathDateUnknown(Boolean.FALSE);
        baseProperties.setBorn(getDate());

        final Gender gender = new Gender();
        gender.setCode("K");
        gender.setDisplayName("Kvinne");
        baseProperties.setGender(gender);

        final Contact contact = new Contact();
        contact.setFirstContact(getDate());
        contact.setLastContact(getDate());
        baseProperties.setContact(contact);
        medicalRecord.setBaseProperties(baseProperties);

        return medicalRecord;
    }

    private DateOrYear getDate() {
        final DateOrYear date = new DateOrYear();
        date.setAar(2000);
        
        return date;
    }
    
}