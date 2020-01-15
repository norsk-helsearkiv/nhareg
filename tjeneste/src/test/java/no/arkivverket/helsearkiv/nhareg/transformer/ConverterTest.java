package no.arkivverket.helsearkiv.nhareg.transformer;

import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.*;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.PersonalDataDTO;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordConverter;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordConverterInterface;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateOrYearConverterTest {
    
    private MedicalRecordConverterInterface medicalRecordConverter;

    @Before
    public void setUp() {
        medicalRecordConverter = new MedicalRecordConverter();
    }
    
    @Test
    public void toMedicalRecordTest() {
        final PersonalDataDTO dto = new PersonalDataDTO();
        dto.setDead("2009");
        dto.setPid("01010942345");
        dto.setBorn("2009");
        dto.setRecordNumber("123");
        dto.setGender("K");
        String[] lagringsenheter = {"boks1"};
        dto.setStorageUnits(lagringsenheter);
        dto.setSerialNumber("234");
        dto.setName("Natalie");
        dto.setUuid("uuid1");
        dto.setFirstContact("2009");
        dto.setLastContact("2009");

        MedicalRecord medicalRecord = medicalRecordConverter.fromPersonalDataDTO(dto);

        assertEquals(dto.getPid(), medicalRecord.getBaseProperties().getIdentifikator().getPid());
        final int dod = medicalRecord.getBaseProperties().getDead().getAar();
        assertEquals(2009, dod);
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

        final RecordId recordId = new RecordId();
        recordId.setRecordNumber("123");
        recordId.setSerialNumber("234");
        medicalRecord.setRecordId(recordId);

        medicalRecord.setUuid("uuid1");
        
        return medicalRecord;
    }

    private DateOrYear getDate() {
        final DateOrYear date = new DateOrYear();
        date.setAar(2000);
        
        return date;
    }
    
}