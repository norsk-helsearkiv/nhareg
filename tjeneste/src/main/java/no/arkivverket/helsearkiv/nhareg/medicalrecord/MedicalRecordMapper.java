package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisMapper;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MedicalRecordMapper {
    
    public MedicalRecordMapper() {}
    
    public static PersondataDTO mapToPersonalDataDTO(Pasientjournal medicalRecord) {
        final PersondataDTO personalData = new PersondataDTO();

        personalData.setUuid(medicalRecord.getUuid());
        personalData.setMerknad(medicalRecord.getMerknad());
        personalData.setFanearkid(medicalRecord.getFanearkid());

        final List<Lagringsenhet> storageUnits = medicalRecord.getLagringsenhet();
        if (storageUnits != null && !storageUnits.isEmpty()) {
            // Converts storageUnits to a String array of IDs.
            final String[] units = storageUnits.stream().map(Lagringsenhet::getIdentifikator).toArray(String[]::new);
            personalData.setLagringsenheter(units);
        }

        final Grunnopplysninger baseProperties = medicalRecord.getGrunnopplysninger();
        if (baseProperties != null) {
            personalData.setNavn(baseProperties.getPnavn());

            if (baseProperties.getIdentifikator() != null) {
                personalData.setFodselsnummer(baseProperties.getIdentifikator().getPID());
            }

            if (baseProperties.getKjønn() != null) {
                personalData.setKjonn(baseProperties.getKjønn().getCode());
            }

            if (baseProperties.getFødt() != null) {
                personalData.setFodt(baseProperties.getFødt().getStringValue());
            }
            
            if (baseProperties.getDød() != null) {
                personalData.setDod(baseProperties.getDød().getStringValue());
            }

            if (baseProperties.getDødsdatoUkjent() != null && baseProperties.getDødsdatoUkjent()) {
                personalData.setDod("mors");
            }

            if (baseProperties.getFodtdatoUkjent() != null && baseProperties.getFodtdatoUkjent()) {
                personalData.setFodt("ukjent");
            }

            if (baseProperties.getKontakt() != null) {
                final DatoEllerAar firstContactDate = baseProperties.getKontakt().getFoerste();
                if (firstContactDate != null) {
                    personalData.setFKontakt(firstContactDate.getStringValue());
                }

                final DatoEllerAar lastContactDate = baseProperties.getKontakt().getSiste();
                if (lastContactDate != null) {
                    personalData.setSKontakt(lastContactDate.getStringValue());
                }
            }
        }

        final Journalidentifikator journalId = medicalRecord.getJournalidentifikator();
        if (journalId != null) {
            personalData.setLopenummer(journalId.getLøpenummer());
            personalData.setJournalnummer(journalId.getJournalnummer());
        }

        return personalData;
    }
    
    public static MedicalRecordDTO mapToMedicalRecordDTO(final Pasientjournal medicalRecord, 
                                                         final TransferDTO transferDTO,
                                                         final String business) {
        final PersondataDTO personalData = mapToPersonalDataDTO(medicalRecord);
        final MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO(); 

        medicalRecordDTO.setPersondata(personalData);
        medicalRecordDTO.setAvleveringBeskrivelse(transferDTO.getTransferDescription());
        medicalRecordDTO.setAvleveringsidentifikator(transferDTO.getAgreementId());
        medicalRecordDTO.setAvleveringLaast(transferDTO.isLocked());
        
        final Set<Diagnose> diagnosisSet = medicalRecord.getDiagnose();
        final List<DiagnoseDTO> diagnoseDTOList = DiagnosisMapper.mapToDiagnosisDTOList(diagnosisSet);
        medicalRecordDTO.setDiagnoser(diagnoseDTOList);
        
        medicalRecordDTO.setVirksomhet(business);
        
        return medicalRecordDTO;
    }
    
    public static RecordTransferDTO mapToRecordTransferDTO(Pasientjournal pasientjournal) {
        RecordTransferDTO recordTransferDTO = new RecordTransferDTO();

        final Grunnopplysninger baseInformation = pasientjournal.getGrunnopplysninger();
        if (baseInformation != null) {
            recordTransferDTO.setNavn(baseInformation.getPnavn());

            if (baseInformation.getIdentifikator() != null) {
                recordTransferDTO.setFodselsnummer(baseInformation.getIdentifikator().getPID());
            }

            final DatoEllerAar born = baseInformation.getFødt();
            if (born != null) {
                final String yearBorn = String.valueOf(born.getYear());
                recordTransferDTO.setFaar(yearBorn);
            }

            if (baseInformation.getFodtdatoUkjent() != null &&
                baseInformation.getFodtdatoUkjent()) {
                recordTransferDTO.setFaar("ukjent");
            }

            final DatoEllerAar dead = baseInformation.getDød();
            if (dead != null) {
                final String yearDied = String.valueOf(dead.getYear());
                recordTransferDTO.setDaar(yearDied);
            }

            if (baseInformation.getDødsdatoUkjent() != null &&
                baseInformation.getDødsdatoUkjent()) {
                recordTransferDTO.setDaar("mors");
            }
        }

        final Journalidentifikator journalId = pasientjournal.getJournalidentifikator();
        if (journalId != null) {
            recordTransferDTO.setJnr(journalId.getJournalnummer());
            recordTransferDTO.setLnr(journalId.getLøpenummer());
        }

        recordTransferDTO.setFanearkid(Integer.parseInt(pasientjournal.getFanearkid()));

        final List<Lagringsenhet> storageUnitList = pasientjournal.getLagringsenhet();
        if (storageUnitList != null && storageUnitList.size() > 0) {
            recordTransferDTO.setLagringsenhet(storageUnitList.get(0).getIdentifikator());
        }

        if (pasientjournal.getOppdateringsinfo() != null) {
            recordTransferDTO.setOppdatertAv(pasientjournal.getOppdateringsinfo().getOppdatertAv());

            if (pasientjournal.getOppdateringsinfo().getSistOppdatert() != null) {
                try {
                    recordTransferDTO.setOpprettetDato(pasientjournal.getOppdateringsinfo().getSistOppdatert().getTimeInMillis());
                } catch (Throwable ignored) {}
            } else {
                recordTransferDTO.setOpprettetDato(0L);
            }
        }

        recordTransferDTO.setUuid(pasientjournal.getUuid());

        return recordTransferDTO;
    }

    public static List<RecordTransferDTO> mapToRecordTransferDTO(List<Pasientjournal> pasientjournalList) {
        return pasientjournalList.stream()
            .map(MedicalRecordMapper::mapToRecordTransferDTO)
            .collect(Collectors.toList());
    }
}