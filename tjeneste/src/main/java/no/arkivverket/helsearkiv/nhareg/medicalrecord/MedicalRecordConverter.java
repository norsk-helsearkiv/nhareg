package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverterInterface;
import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisConverter;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.*;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.PersonalDataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.validation.PIDValidation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MedicalRecordConverter implements MedicalRecordConverterInterface {

    private DateOrYearConverterInterface dateOrYearConverter = new DateOrYearConverter();
    
    public MedicalRecord fromPersonalDataDTO(final PersonalDataDTO personalDataDTO) {
        if (personalDataDTO == null) {
            return null;
        }
        
        final MedicalRecord medicalRecord = new MedicalRecord();

        final String uuid = personalDataDTO.getUuid();
        medicalRecord.setUuid(uuid);

        final String[] storageUnits = personalDataDTO.getStorageUnits();
        final List<StorageUnit> storageUnitList = medicalRecord.getStorageUnit();
        if (storageUnits != null) {
            // For each storage unit: create a new StorageUnit with random UUID, then add it to the list.
            Arrays.stream(storageUnits).forEach(
                unitId -> {
                    final StorageUnit storageUnit = new StorageUnit(unitId, null, false);
                    storageUnitList.add(storageUnit);
                }
            );
        }

        final String recordNumber = personalDataDTO.getRecordNumber();
        if (recordNumber != null) {
            medicalRecord.setRecordNumber(recordNumber);
        }

        final String serialNumber = personalDataDTO.getSerialNumber();
        if (serialNumber != null) {
            medicalRecord.setSerialNumber(serialNumber);
        }

        final String fanearkid = personalDataDTO.getFanearkid();
        if (fanearkid != null) {
            medicalRecord.setFanearkid(fanearkid);
        }

        final BaseProperties baseProperties = new BaseProperties();
        final String pid = personalDataDTO.getPid();
        if (pid != null) {
            final Identifikator identifikator = new Identifikator();
            identifikator.setPid(pid);

            if (PIDValidation.isHnummer(pid)) {
                identifikator.setTypePID("H");
            } else if (PIDValidation.isDnummer(pid)) {
                identifikator.setTypePID("D");
            } else if(PIDValidation.isFnummer(pid)) {
                identifikator.setTypePID("F");
            }

            baseProperties.setIdentifikator(identifikator);
        }

        final String name = personalDataDTO.getName();
        if (name != null) {
            baseProperties.setName(name);
        }

        final String genderString = personalDataDTO.getGender();
        if (genderString != null) {
            final Gender gender = new Gender();
            gender.setCode(genderString);
            baseProperties.setGender(gender);
        }

        final String born = personalDataDTO.getBorn();
        if (born != null) {
            baseProperties.setBorn(dateOrYearConverter.toDateOrYear(born));
        }

        final String dead = personalDataDTO.getDead();
        if (dead != null) {
            baseProperties.setDead(dateOrYearConverter.toDateOrYear(dead));
        }

        baseProperties.setDeathDateUnknown(baseProperties.getDead() == null);
        baseProperties.setBornDateUnknown(baseProperties.getBorn() == null);

        final Contact contact = new Contact();
        final String firstContact = personalDataDTO.getFirstContact();
        if (firstContact != null) {
            contact.setFirstContact(dateOrYearConverter.toDateOrYear(firstContact));
        }

        final String lastContact = personalDataDTO.getLastContact();
        if (lastContact != null) {
            contact.setLastContact(dateOrYearConverter.toDateOrYear(lastContact));
        }

        baseProperties.setContact(contact);
        medicalRecord.setBaseProperties(baseProperties);
        medicalRecord.setNote(personalDataDTO.getNote());

        return medicalRecord;
    }

    public PersonalDataDTO toPersonalDataDTO(final MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return null;
        }
        
        final PersonalDataDTO personalData = new PersonalDataDTO();

        personalData.setUuid(medicalRecord.getUuid());
        personalData.setNote(medicalRecord.getNote());
        personalData.setFanearkid(medicalRecord.getFanearkid());
        personalData.setSerialNumber(medicalRecord.getSerialNumber());
        personalData.setRecordNumber(medicalRecord.getRecordNumber());

        final List<StorageUnit> storageUnits = medicalRecord.getStorageUnit();
        if (storageUnits != null && !storageUnits.isEmpty()) {
            // Converts storageUnits to a String array of IDs.
            final String[] units = storageUnits.stream().map(StorageUnit::getId).toArray(String[]::new);
            personalData.setStorageUnits(units);
        }

        final BaseProperties baseProperties = medicalRecord.getBaseProperties();
        if (baseProperties != null) {
            personalData.setName(baseProperties.getName());

            if (baseProperties.getIdentifikator() != null) {
                personalData.setPid(baseProperties.getIdentifikator().getPid());
            }

            if (baseProperties.getGender() != null) {
                personalData.setGender(baseProperties.getGender().getCode());
            }

            final DateOrYear born = baseProperties.getBorn();
            if (born != null) {
                personalData.setBorn(dateOrYearConverter.fromDateOrYear(born));
            }

            final DateOrYear dead = baseProperties.getDead();
            if (dead != null) {
                personalData.setDead(dateOrYearConverter.fromDateOrYear(dead));
            }

            if (baseProperties.getDeathDateUnknown() != null && baseProperties.getDeathDateUnknown()) {
                personalData.setDead("mors");
            }

            if (baseProperties.getBornDateUnknown() != null && baseProperties.getBornDateUnknown()) {
                personalData.setBorn("ukjent");
            }

            if (baseProperties.getContact() != null) {
                final DateOrYear firstContactDate = baseProperties.getContact().getFirstContact();
                if (firstContactDate != null) {
                    personalData.setFirstContact(dateOrYearConverter.fromDateOrYear(firstContactDate));
                }

                final DateOrYear lastContactDate = baseProperties.getContact().getLastContact();
                if (lastContactDate != null) {
                    personalData.setLastContact(dateOrYearConverter.fromDateOrYear(lastContactDate));
                }
            }
        }
        
        return personalData;
    }

    public MedicalRecordDTO toMedicalRecordDTO(final MedicalRecord medicalRecord,
                                               final Transfer transfer,
                                               final String business) {
        if (medicalRecord == null) {
            return null;
        }
        
        final PersonalDataDTO personalData = toPersonalDataDTO(medicalRecord);
        final MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();

        medicalRecordDTO.setPersonalDataDTO(personalData);
        medicalRecordDTO.setTransferDescription(transfer.getTransferDescription());
        medicalRecordDTO.setTransferId(transfer.getTransferId());
        medicalRecordDTO.setTransferLocked(transfer.isLocked());

        final Set<Diagnosis> diagnosisSet = medicalRecord.getDiagnosis();
        final List<DiagnoseDTO> diagnoseDTOList = new DiagnosisConverter().toDiagnosisDTOList(diagnosisSet);
        medicalRecordDTO.setDiagnosisDTOList(diagnoseDTOList);

        medicalRecordDTO.setBusiness(business);

        return medicalRecordDTO;
    }

    public RecordTransferDTO toRecordTransferDTO(final MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return null;
        }
        
        final RecordTransferDTO recordTransferDTO = new RecordTransferDTO();

        final BaseProperties baseInformation = medicalRecord.getBaseProperties();
        if (baseInformation != null) {
            recordTransferDTO.setName(baseInformation.getName());

            if (baseInformation.getIdentifikator() != null) {
                recordTransferDTO.setPid(baseInformation.getIdentifikator().getPid());
            }

            final DateOrYear born = baseInformation.getBorn();
            if (born != null) {
                final String yearBorn = String.valueOf(born.getAsYear());
                recordTransferDTO.setBornYear(yearBorn);
            }

            if (baseInformation.getBornDateUnknown() != null &&
                baseInformation.getBornDateUnknown()) {
                recordTransferDTO.setBornYear("ukjent");
            }

            final DateOrYear dead = baseInformation.getDead();
            if (dead != null) {
                final String yearDied = String.valueOf(dead.getAsYear());
                recordTransferDTO.setDeathYear(yearDied);
            }

            if (baseInformation.getDeathDateUnknown() != null &&
                baseInformation.getDeathDateUnknown()) {
                recordTransferDTO.setDeathYear("mors");
            }
        }

        recordTransferDTO.setRecordNumber(medicalRecord.getRecordNumber());
        recordTransferDTO.setSerialNumber(medicalRecord.getSerialNumber());
        recordTransferDTO.setFanearkid(Long.parseLong(medicalRecord.getFanearkid()));

        final List<StorageUnit> storageUnitList = medicalRecord.getStorageUnit();
        if (storageUnitList != null && storageUnitList.size() > 0) {
            recordTransferDTO.setStorageUnit(storageUnitList.get(0).getId());
        }

        if (medicalRecord.getUpdateInfo() != null) {
            recordTransferDTO.setUpdatedBy(medicalRecord.getUpdateInfo().getUpdatedBy());

            if (medicalRecord.getUpdateInfo().getLastUpdated() != null) {
                try {
                    recordTransferDTO.setCreationDate(medicalRecord.getUpdateInfo().getLastUpdated().getTimeInMillis());
                } catch (Throwable ignored) {}
            } else {
                recordTransferDTO.setCreationDate(0L);
            }
        }

        recordTransferDTO.setUuid(medicalRecord.getUuid());

        return recordTransferDTO;
    }

    public List<RecordTransferDTO> toRecordTransferDTOList(final List<MedicalRecord> medicalRecordList) {
        if (medicalRecordList == null) {
            return null;
        }
        
        return medicalRecordList.stream().map(this::toRecordTransferDTO).collect(Collectors.toList());
    }
    
}