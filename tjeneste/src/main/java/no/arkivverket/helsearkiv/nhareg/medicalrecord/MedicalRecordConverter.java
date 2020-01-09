package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisConverter;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.validation.PIDValidation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter.toDateOrYear;

public class MedicalRecordConverter {
    
    public static MedicalRecord convertFromPersonalDataDTO(final PersondataDTO personalDataDTO) {
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
        
        final RecordId journalId = new RecordId();
        medicalRecord.setRecordId(journalId);

        final String recordNumber = personalDataDTO.getRecordNumber();
        if (recordNumber != null) {
            journalId.setRecordNumber(recordNumber);
        }

        final String serialNumber = personalDataDTO.getSerialNumber();
        if (serialNumber != null) {
            journalId.setSerialNumber(serialNumber);
        }

        final String fanearkid = personalDataDTO.getFanearkid();
        if (fanearkid != null) {
            medicalRecord.setFanearkid(fanearkid);
        }

        final Grunnopplysninger baseProperties = new Grunnopplysninger();
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
            baseProperties.setPnavn(name);
        }

        final String genderString = personalDataDTO.getGender();
        if (genderString != null) {
            final Gender gender = new Gender();
            gender.setCode(genderString);
            baseProperties.setGender(gender);
        }

        final String born = personalDataDTO.getBorn();
        if (born != null) {
            baseProperties.setBorn(toDateOrYear(born));
        }

        final String dead = personalDataDTO.getDead();
        if (dead != null) {
            baseProperties.setDead(toDateOrYear(dead));
        }

        baseProperties.setDeathDateUnknown(baseProperties.getDead() == null);
        baseProperties.setBornDateUnknown(baseProperties.getBorn() == null);

        final Kontakt contact = new Kontakt();
        final String firstContact = personalDataDTO.getFirstContact();
        if (firstContact != null) {
            contact.setFoerste(toDateOrYear(firstContact));
        }

        final String lastContact = personalDataDTO.getLastContact();
        if (lastContact != null) {
            contact.setSiste(toDateOrYear(lastContact));
        }

        baseProperties.setKontakt(contact);
        medicalRecord.setGrunnopplysninger(baseProperties);
        medicalRecord.setMerknad(personalDataDTO.getNote());

        return medicalRecord;
    }
    
    public static PersondataDTO convertToPersonalDataDTO(final MedicalRecord medicalRecord) {
        final PersondataDTO personalData = new PersondataDTO();

        personalData.setUuid(medicalRecord.getUuid());
        personalData.setNote(medicalRecord.getMerknad());
        personalData.setFanearkid(medicalRecord.getFanearkid());

        final List<StorageUnit> storageUnits = medicalRecord.getStorageUnit();
        if (storageUnits != null && !storageUnits.isEmpty()) {
            // Converts storageUnits to a String array of IDs.
            final String[] units = storageUnits.stream().map(StorageUnit::getId).toArray(String[]::new);
            personalData.setStorageUnits(units);
        }

        final Grunnopplysninger baseProperties = medicalRecord.getGrunnopplysninger();
        if (baseProperties != null) {
            personalData.setName(baseProperties.getPnavn());

            if (baseProperties.getIdentifikator() != null) {
                personalData.setPid(baseProperties.getIdentifikator().getPid());
            }

            if (baseProperties.getGender() != null) {
                personalData.setGender(baseProperties.getGender().getCode());
            }

            if (baseProperties.getBorn() != null) {
                personalData.setBorn(baseProperties.getBorn().getStringValue());
            }
            
            if (baseProperties.getDead() != null) {
                personalData.setDead(baseProperties.getDead().getStringValue());
            }

            if (baseProperties.getDeathDateUnknown() != null && baseProperties.getDeathDateUnknown()) {
                personalData.setDead("mors");
            }

            if (baseProperties.getBornDateUnknown() != null && baseProperties.getBornDateUnknown()) {
                personalData.setBorn("ukjent");
            }

            if (baseProperties.getKontakt() != null) {
                final DateOrYear firstContactDate = baseProperties.getKontakt().getFoerste();
                if (firstContactDate != null) {
                    personalData.setFirstContact(firstContactDate.getStringValue());
                }

                final DateOrYear lastContactDate = baseProperties.getKontakt().getSiste();
                if (lastContactDate != null) {
                    personalData.setLastContact(lastContactDate.getStringValue());
                }
            }
        }

        final RecordId journalId = medicalRecord.getRecordId();
        if (journalId != null) {
            personalData.setSerialNumber(journalId.getSerialNumber());
            personalData.setRecordNumber(journalId.getRecordNumber());
        }

        return personalData;
    }
    
    public static MedicalRecordDTO convertToMedicalRecordDTO(final MedicalRecord medicalRecord,
                                                             final Transfer transfer,
                                                             final String business) {
        final PersondataDTO personalData = convertToPersonalDataDTO(medicalRecord);
        final MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO(); 

        medicalRecordDTO.setPersonalDataDTO(personalData);
        medicalRecordDTO.setTransferDescription(transfer.getTransferDescription());
        medicalRecordDTO.setTransferId(transfer.getTransferId());
        medicalRecordDTO.setTransferLocked(transfer.isLocked());
        
        final Set<Diagnosis> diagnosisSet = medicalRecord.getDiagnosis();
        final List<DiagnoseDTO> diagnoseDTOList = DiagnosisConverter.convertToDiagnosisDTOList(diagnosisSet);
        medicalRecordDTO.setDiagnosisDTOList(diagnoseDTOList);
        
        medicalRecordDTO.setBusiness(business);
        
        return medicalRecordDTO;
    }
    
    public static RecordTransferDTO convertToRecordTransferDTO(final MedicalRecord medicalRecord) {
        RecordTransferDTO recordTransferDTO = new RecordTransferDTO();

        final Grunnopplysninger baseInformation = medicalRecord.getGrunnopplysninger();
        if (baseInformation != null) {
            recordTransferDTO.setName(baseInformation.getPnavn());

            if (baseInformation.getIdentifikator() != null) {
                recordTransferDTO.setPid(baseInformation.getIdentifikator().getPid());
            }

            final DateOrYear born = baseInformation.getBorn();
            if (born != null) {
                final String yearBorn = String.valueOf(born.getYear());
                recordTransferDTO.setBornYear(yearBorn);
            }

            if (baseInformation.getBornDateUnknown() != null &&
                baseInformation.getBornDateUnknown()) {
                recordTransferDTO.setBornYear("ukjent");
            }

            final DateOrYear dead = baseInformation.getDead();
            if (dead != null) {
                final String yearDied = String.valueOf(dead.getYear());
                recordTransferDTO.setDeathYear(yearDied);
            }

            if (baseInformation.getDeathDateUnknown() != null &&
                baseInformation.getDeathDateUnknown()) {
                recordTransferDTO.setDeathYear("mors");
            }
        }

        final RecordId journalId = medicalRecord.getRecordId();
        if (journalId != null) {
            recordTransferDTO.setRecordNumber(journalId.getRecordNumber());
            recordTransferDTO.setSerialNumber(journalId.getSerialNumber());
        }

        recordTransferDTO.setFanearkid(Long.parseLong(medicalRecord.getFanearkid()));

        final List<StorageUnit> storageUnitList = medicalRecord.getStorageUnit();
        if (storageUnitList != null && storageUnitList.size() > 0) {
            recordTransferDTO.setStorageUnit(storageUnitList.get(0).getId());
        }

        if (medicalRecord.getOppdateringsinfo() != null) {
            recordTransferDTO.setUpdatedBy(medicalRecord.getOppdateringsinfo().getOppdatertAv());

            if (medicalRecord.getOppdateringsinfo().getSistOppdatert() != null) {
                try {
                    recordTransferDTO.setCreationDate(medicalRecord.getOppdateringsinfo().getSistOppdatert().getTimeInMillis());
                } catch (Throwable ignored) {}
            } else {
                recordTransferDTO.setCreationDate(0L);
            }
        }

        recordTransferDTO.setUuid(medicalRecord.getUuid());

        return recordTransferDTO;
    }

    public static List<RecordTransferDTO> convertToRecordTransferDTOList(final List<MedicalRecord> medicalRecordList) {
        return medicalRecordList.stream()
            .map(MedicalRecordConverter::convertToRecordTransferDTO)
            .collect(Collectors.toList());
    }
}