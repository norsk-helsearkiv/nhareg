package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.archiveauthor.ArchiveAuthorConverter;
import no.arkivverket.helsearkiv.nhareg.archiveauthor.ArchiveAuthorConverterInterface;
import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverterInterface;
import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisConverter;
import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisConverterInterface;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.*;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.PersonalDataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.validation.PIDValidation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MedicalRecordConverter implements MedicalRecordConverterInterface {

    private DateOrYearConverterInterface dateOrYearConverter = new DateOrYearConverter();

    private DiagnosisConverterInterface diagnosisConverter = new DiagnosisConverter();
    
    private ArchiveAuthorConverterInterface archiveAuthorConverter = new ArchiveAuthorConverter();
    
    @Override
    public MedicalRecord fromPersonalDataDTO(final PersonalDataDTO personalDataDTO) {
        if (personalDataDTO == null) {
            return null;
        }

        final MedicalRecord medicalRecord = new MedicalRecord();

        final String uuid = personalDataDTO.getUuid();
        final String recordNumber = personalDataDTO.getRecordNumber();
        final String serialNumber = personalDataDTO.getSerialNumber();
        final Long fanearkid = personalDataDTO.getFanearkid();
        final String pid = personalDataDTO.getPid();
        final String name = personalDataDTO.getName();
        final String genderString = personalDataDTO.getGender();
        final Gender gender = new Gender();
        final String born = personalDataDTO.getBorn();
        final String dead = personalDataDTO.getDead();
        final String firstContact = personalDataDTO.getFirstContact();
        final String lastContact = personalDataDTO.getLastContact();
        final Set<ArchiveAuthor> authors = archiveAuthorConverter.toArchiveAuthorSet(personalDataDTO.getArchiveAuthors());

        medicalRecord.setArchiveAuthors(authors);
        medicalRecord.setUuid(uuid);
        medicalRecord.setNote(personalDataDTO.getNote());
        medicalRecord.setRecordNumber(recordNumber);
        medicalRecord.setSerialNumber(serialNumber);
        medicalRecord.setFanearkid(fanearkid == null ? null : fanearkid.toString());
        medicalRecord.setPid(pid);
        medicalRecord.setName(name);
        medicalRecord.setGender(gender);
        gender.setCode(genderString);
        medicalRecord.setBorn(dateOrYearConverter.toDateOrYear(born));
        medicalRecord.setDead(dateOrYearConverter.toDateOrYear(dead));
        medicalRecord.setDeathDateUnknown(medicalRecord.getDead() == null);
        medicalRecord.setBornDateUnknown(medicalRecord.getBorn() == null);
        medicalRecord.setFirstContact(dateOrYearConverter.toDateOrYear(firstContact));
        medicalRecord.setLastContact(dateOrYearConverter.toDateOrYear(lastContact));

        if (PIDValidation.isHnummer(pid)) {
            medicalRecord.setTypePID("H");
        } else if (PIDValidation.isDnummer(pid)) {
            medicalRecord.setTypePID("D");
        } else if(PIDValidation.isFnummer(pid)) {
            medicalRecord.setTypePID("F");
        }

        final String[] storageUnits = personalDataDTO.getStorageUnits();
        final Set<StorageUnit> storageUnitList = medicalRecord.getStorageUnit();
        if (storageUnits != null) {
            // For each storage unit: create a new StorageUnit with random UUID, then add it to the list.
            Arrays.stream(storageUnits).forEach(
                unitId -> {
                    final StorageUnit storageUnit = new StorageUnit(unitId, null, false);
                    storageUnitList.add(storageUnit);
                }
            );
        }

        return medicalRecord;
    }

    @Override
    public PersonalDataDTO toPersonalDataDTO(final MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return null;
        }

        final PersonalDataDTO personalData = new PersonalDataDTO();
        final DateOrYear born = medicalRecord.getBorn();
        final DateOrYear dead = medicalRecord.getDead();
        final DateOrYear firstContactDate = medicalRecord.getFirstContact();
        final DateOrYear lastContactDate = medicalRecord.getLastContact();
        final Set<ArchiveAuthor> authors = medicalRecord.getArchiveAuthors();

        personalData.setArchiveAuthors(archiveAuthorConverter.fromArchiveAuthorCollection(authors));
        personalData.setUuid(medicalRecord.getUuid());
        personalData.setNote(medicalRecord.getNote());
        personalData.setSerialNumber(medicalRecord.getSerialNumber());
        personalData.setRecordNumber(medicalRecord.getRecordNumber());
        personalData.setName(medicalRecord.getName());
        personalData.setPid(medicalRecord.getPid());
        personalData.setBorn(dateOrYearConverter.fromDateOrYear(born));
        personalData.setDead(dateOrYearConverter.fromDateOrYear(dead));
        personalData.setFirstContact(dateOrYearConverter.fromDateOrYear(firstContactDate));
        personalData.setLastContact(dateOrYearConverter.fromDateOrYear(lastContactDate));

        final String fanearkid = medicalRecord.getFanearkid();
        if (fanearkid != null && !fanearkid.isEmpty()) {
            personalData.setFanearkid(Long.parseLong(fanearkid));
        }
        
        final Set<StorageUnit> storageUnits = medicalRecord.getStorageUnit();
        if (storageUnits != null && !storageUnits.isEmpty()) {
            // Converts storageUnits to a String array of IDs.
            final String[] units = storageUnits.stream().map(StorageUnit::getId).toArray(String[]::new);
            personalData.setStorageUnits(units);
        }

        final Gender gender = medicalRecord.getGender();
        if (gender != null) {
            personalData.setGender(gender.getCode());
        }

        final Boolean deathDateUnknown = medicalRecord.getDeathDateUnknown();
        if (deathDateUnknown != null && deathDateUnknown) {
            personalData.setDead("mors");
        }

        final Boolean bornDateUnknown = medicalRecord.getBornDateUnknown();
        if (bornDateUnknown != null && bornDateUnknown) {
            personalData.setBorn("ukjent");
        }

        return personalData;
    }

    @Override
    public MedicalRecordDTO toMedicalRecordDTO(final MedicalRecord medicalRecord,
                                               final Transfer transfer,
                                               final String business) {
        if (medicalRecord == null) {
            return null;
        }

        final PersonalDataDTO personalData = toPersonalDataDTO(medicalRecord);
        final MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        final Set<Diagnosis> diagnosisSet = medicalRecord.getDiagnosis();
        final Set<DiagnosisDTO> diagnoses = diagnosisConverter.toDiagnosisDTOSet(diagnosisSet);
        
        medicalRecordDTO.setPersonalDataDTO(personalData);
        medicalRecordDTO.setTransferDescription(transfer.getTransferDescription());
        medicalRecordDTO.setTransferId(transfer.getTransferId());
        medicalRecordDTO.setTransferLocked(transfer.isLocked());
        medicalRecordDTO.setDiagnoses(diagnoses);
        medicalRecordDTO.setBusiness(business);

        return medicalRecordDTO;
    }

    @Override
    public RecordTransferDTO toRecordTransferDTO(final MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return null;
        }

        final RecordTransferDTO recordTransferDTO = new RecordTransferDTO();
        recordTransferDTO.setUuid(medicalRecord.getUuid());
        recordTransferDTO.setName(medicalRecord.getName());
        recordTransferDTO.setPid(medicalRecord.getPid());
        recordTransferDTO.setRecordNumber(medicalRecord.getRecordNumber());
        recordTransferDTO.setSerialNumber(medicalRecord.getSerialNumber());
        recordTransferDTO.setFanearkid(Long.parseLong(medicalRecord.getFanearkid()));

        final DateOrYear born = medicalRecord.getBorn();
        if (born != null) {
            final Integer asYear = born.getAsYear();
            if (asYear != null) {
                final String yearBorn = String.valueOf(asYear);
                recordTransferDTO.setBornYear(yearBorn);
            }
        }

        final Boolean bornDateUnknown = medicalRecord.getBornDateUnknown();
        if (bornDateUnknown != null && bornDateUnknown) {
            recordTransferDTO.setBornYear("ukjent");
        }

        final DateOrYear dead = medicalRecord.getDead();
        if (dead != null) {
            final Integer asYear = dead.getAsYear();
            if (asYear != null) {
                final String yearDied = String.valueOf(asYear);
                recordTransferDTO.setDeathYear(yearDied);
            }
        }

        final Boolean deathDateUnknown = medicalRecord.getDeathDateUnknown();
        if (deathDateUnknown != null && deathDateUnknown) {
            recordTransferDTO.setDeathYear("mors");
        }

        final Set<StorageUnit> storageUnitList = medicalRecord.getStorageUnit();
        if (storageUnitList != null && storageUnitList.size() > 0) {
            final String storageUnitsString = storageUnitList.stream().distinct()
                                                             .map(StorageUnit::getId)
                                                             .collect(Collectors.joining(", "));
            recordTransferDTO.setStorageUnits(storageUnitsString);
        }

        final UpdateInfo updateInfo = medicalRecord.getUpdateInfo();
        if (updateInfo != null) {
            recordTransferDTO.setUpdatedBy(updateInfo.getUpdatedBy());

            final LocalDateTime lastUpdated = updateInfo.getLastUpdated();
            if (lastUpdated != null) {
                try {
                    final long epochSecond = lastUpdated.atZone(ZoneId.systemDefault())
                                                        .toInstant()
                                                        .getEpochSecond();
                    recordTransferDTO.setCreationDate(epochSecond);
                } catch (Throwable ignored) {}
            } else {
                recordTransferDTO.setCreationDate(0L);
            }
        }

        return recordTransferDTO;
    }

    @Override
    public List<RecordTransferDTO> toRecordTransferDTOList(final Collection<MedicalRecord> medicalRecordList) {
        if (medicalRecordList == null) {
            return null;
        }

        return medicalRecordList.stream().map(this::toRecordTransferDTO).collect(Collectors.toList());
    }

}