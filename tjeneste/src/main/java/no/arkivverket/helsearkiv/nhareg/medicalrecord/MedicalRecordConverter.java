package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.archiveauthor.ArchiveAuthorConverter;
import no.arkivverket.helsearkiv.nhareg.archiveauthor.ArchiveAuthorConverterInterface;
import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverterInterface;
import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisConverter;
import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisConverterInterface;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.*;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.ArchiveAuthorDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
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

    private final DateOrYearConverterInterface dateOrYearConverter = new DateOrYearConverter();

    private final DiagnosisConverterInterface diagnosisConverter = new DiagnosisConverter();
    
    private final ArchiveAuthorConverterInterface archiveAuthorConverter = new ArchiveAuthorConverter();
    
    @Override
    public MedicalRecord fromMedicalRecordDTO(final MedicalRecordDTO medicalRecordDTO) {
        if (medicalRecordDTO == null) {
            return null;
        }

        final MedicalRecord medicalRecord = new MedicalRecord();

        final String uuid = medicalRecordDTO.getUuid();
        final String recordNumber = medicalRecordDTO.getRecordNumber();
        final String serialNumber = medicalRecordDTO.getSerialNumber();
        final Long fanearkid = medicalRecordDTO.getFanearkid();
        final String pid = medicalRecordDTO.getPid();
        final String name = medicalRecordDTO.getName();
        final String genderString = medicalRecordDTO.getGender();
        final Gender gender = new Gender();
        final String born = medicalRecordDTO.getBorn();
        final String dead = medicalRecordDTO.getDead();
        final String firstContact = medicalRecordDTO.getFirstContact();
        final String lastContact = medicalRecordDTO.getLastContact();
        final Set<ArchiveAuthorDTO> archiveAuthors = medicalRecordDTO.getArchiveAuthors();
        final Set<ArchiveAuthor> authors = archiveAuthorConverter.toArchiveAuthorSet(archiveAuthors);

        medicalRecord.setArchiveAuthors(authors);
        medicalRecord.setUuid(uuid);
        medicalRecord.setNote(medicalRecordDTO.getNote());
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

        if (PIDValidation.isHNumber(pid)) {
            medicalRecord.setTypePID("H");
        } else if (PIDValidation.isDNumber(pid)) {
            medicalRecord.setTypePID("D");
        } else if(PIDValidation.isFNumber(pid)) {
            medicalRecord.setTypePID("F");
        }

        final String[] storageUnits = medicalRecordDTO.getStorageUnits();
        if (storageUnits != null) {
            final Set<StorageUnit> newUnits = Arrays.stream(storageUnits)
                                                    .map(id -> new StorageUnit(id, null, false))
                                                    .collect(Collectors.toSet());
            
            medicalRecord.getStorageUnits().addAll(newUnits);
        }

        return medicalRecord;
    }

    @Override
    public MedicalRecordDTO toMedicalRecordDTO(final MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return null;
        }

        final MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        final DateOrYear born = medicalRecord.getBorn();
        final DateOrYear dead = medicalRecord.getDead();
        final DateOrYear firstContactDate = medicalRecord.getFirstContact();
        final DateOrYear lastContactDate = medicalRecord.getLastContact();
        final Set<ArchiveAuthor> authors = medicalRecord.getArchiveAuthors();
        final Set<Diagnosis> diagnosisSet = medicalRecord.getDiagnosis();
        final Set<DiagnosisDTO> diagnoses = diagnosisConverter.toDiagnosisDTOSet(diagnosisSet);

        medicalRecordDTO.setDiagnoses(diagnoses);
        medicalRecordDTO.setArchiveAuthors(archiveAuthorConverter.fromArchiveAuthorCollection(authors));
        medicalRecordDTO.setUuid(medicalRecord.getUuid());
        medicalRecordDTO.setNote(medicalRecord.getNote());
        medicalRecordDTO.setSerialNumber(medicalRecord.getSerialNumber());
        medicalRecordDTO.setRecordNumber(medicalRecord.getRecordNumber());
        medicalRecordDTO.setName(medicalRecord.getName());
        medicalRecordDTO.setPid(medicalRecord.getPid());
        medicalRecordDTO.setBorn(dateOrYearConverter.fromDateOrYear(born));
        medicalRecordDTO.setDead(dateOrYearConverter.fromDateOrYear(dead));
        medicalRecordDTO.setFirstContact(dateOrYearConverter.fromDateOrYear(firstContactDate));
        medicalRecordDTO.setLastContact(dateOrYearConverter.fromDateOrYear(lastContactDate));
        medicalRecordDTO.setDeleted(medicalRecord.getDeleted());

        final Transfer transfer = medicalRecord.getTransfer();
        if (transfer != null) {
            medicalRecordDTO.setTransferLocked(transfer.isLocked());
            medicalRecordDTO.setTransferId(transfer.getTransferId());
        }

        final String fanearkid = medicalRecord.getFanearkid();
        if (fanearkid != null && !fanearkid.isEmpty()) {
            medicalRecordDTO.setFanearkid(Long.parseLong(fanearkid));
        }
        
        final Set<StorageUnit> storageUnits = medicalRecord.getStorageUnits();
        if (storageUnits != null && !storageUnits.isEmpty()) {
            // Converts storageUnits to a String array of IDs.
            final String[] units = storageUnits.stream().map(StorageUnit::getId).toArray(String[]::new);
            medicalRecordDTO.setStorageUnits(units);
        }

        final Gender gender = medicalRecord.getGender();
        if (gender != null) {
            medicalRecordDTO.setGender(gender.getCode());
        }

        final Boolean deathDateUnknown = medicalRecord.getDeathDateUnknown();
        if (deathDateUnknown != null && deathDateUnknown) {
            medicalRecordDTO.setDead("mors");
        }

        final Boolean bornDateUnknown = medicalRecord.getBornDateUnknown();
        if (bornDateUnknown != null && bornDateUnknown) {
            medicalRecordDTO.setBorn("ukjent");
        }

        return medicalRecordDTO;
    }

    @Override
    public List<RecordTransferDTO> toRecordTransferDTOList(final Collection<MedicalRecord> medicalRecordList) {
        if (medicalRecordList == null) {
            return null;
        }

        return medicalRecordList.stream().map(this::toRecordTransferDTO).collect(Collectors.toList());
    }

    private RecordTransferDTO toRecordTransferDTO(final MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return null;
        }

        final RecordTransferDTO recordTransferDTO = new RecordTransferDTO();
        recordTransferDTO.setUuid(medicalRecord.getUuid());
        recordTransferDTO.setName(medicalRecord.getName());
        recordTransferDTO.setPid(medicalRecord.getPid());
        recordTransferDTO.setRecordNumber(medicalRecord.getRecordNumber());
        recordTransferDTO.setSerialNumber(medicalRecord.getSerialNumber());
        recordTransferDTO.setTransferLocked(medicalRecord.getTransfer().isLocked());

        final String fanearkid = medicalRecord.getFanearkid();
        if (fanearkid != null && !fanearkid.isEmpty()) {
            recordTransferDTO.setFanearkid(Long.parseLong(fanearkid));
        }

        final DateOrYear born = medicalRecord.getBorn();
        if (born != null) {
            recordTransferDTO.setBornYear(born.toString());
        }

        final Boolean bornDateUnknown = medicalRecord.getBornDateUnknown();
        if (bornDateUnknown != null && bornDateUnknown) {
            recordTransferDTO.setBornYear("ukjent");
        }

        final DateOrYear dead = medicalRecord.getDead();
        if (dead != null) {
            recordTransferDTO.setDeathYear(dead.toString());
        }

        final Boolean deathDateUnknown = medicalRecord.getDeathDateUnknown();
        if (deathDateUnknown != null && deathDateUnknown) {
            recordTransferDTO.setDeathYear("mors");
        }

        final Set<StorageUnit> storageUnitList = medicalRecord.getStorageUnits();
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

}