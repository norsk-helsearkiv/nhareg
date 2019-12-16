package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisConverter;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.*;
import no.arkivverket.helsearkiv.nhareg.util.PersonnummerValiderer;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter.tilDatoEllerAar;

public class MedicalRecordConverter {
    
    public static Pasientjournal convertFromPersonalDataDTO(final PersondataDTO personalDataDTO) {
        final Pasientjournal medicalRecord = new Pasientjournal();
        final String uuid = personalDataDTO.getUuid();

        medicalRecord.setUuid(uuid);
        
        final String[] storageUnits = personalDataDTO.getLagringsenheter();
        final List<Lagringsenhet> storageUnitList = medicalRecord.getLagringsenhet();
        if (storageUnits != null) {
            // For each storage unit: create a new StorageUnit with random UUID, then add it to the list.
            Arrays.stream(storageUnits).forEach(
                unitId -> {
                    final Lagringsenhet storageUnit = new Lagringsenhet(unitId, UUID.randomUUID().toString(), false);
                    storageUnitList.add(storageUnit);
                }
            );
        }
        
        final Journalidentifikator journalId = new Journalidentifikator();
        medicalRecord.setJournalidentifikator(journalId);

        final String recordNumber = personalDataDTO.getJournalnummer();
        if (recordNumber != null) {
            journalId.setJournalnummer(recordNumber);
        }

        final String serialNumber = personalDataDTO.getLopenummer();
        if (serialNumber != null) {
            journalId.setLøpenummer(serialNumber);
        }

        final String fanearkid = personalDataDTO.getFanearkid();
        if (fanearkid != null) {
            medicalRecord.setFanearkid(fanearkid);
        }

        final Grunnopplysninger baseProperties = new Grunnopplysninger();
        final String pid = personalDataDTO.getFodselsnummer();
        if (pid != null) {
            final Identifikator identifikator = new Identifikator();
            identifikator.setPID(pid);

            if (PersonnummerValiderer.isHnummer(pid)) {
                identifikator.setTypePID("H");
            } else if (PersonnummerValiderer.isDnummer(pid)) {
                identifikator.setTypePID("D");
            } else if(PersonnummerValiderer.isFnummer(pid)) {
                identifikator.setTypePID("F");
            }
            
            baseProperties.setIdentifikator(identifikator);
        }

        final String name = personalDataDTO.getNavn();
        if (name != null) {
            baseProperties.setPnavn(name);
        }

        final String genderString = personalDataDTO.getKjonn();
        if (genderString != null) {
            final Kjønn gender = new Kjønn();
            gender.setCode(genderString);
            baseProperties.setKjønn(gender);
        }

        final String born = personalDataDTO.getFodt();
        if (born != null) {
            baseProperties.setFødt(tilDatoEllerAar(born));
        }

        final String dead = personalDataDTO.getDod();
        if (dead != null) {
            baseProperties.setDød(tilDatoEllerAar(dead));
        }

        baseProperties.setDødsdatoUkjent(baseProperties.getDød() == null);
        baseProperties.setFodtdatoUkjent(baseProperties.getFødt() == null);

        final Kontakt contact = new Kontakt();
        final String firstContact = personalDataDTO.getFKontakt();
        if (firstContact != null) {
            contact.setFoerste(tilDatoEllerAar(firstContact));
        }

        final String lastContact = personalDataDTO.getSKontakt();
        if (lastContact != null) {
            contact.setSiste(tilDatoEllerAar(lastContact));
        }

        baseProperties.setKontakt(contact);
        medicalRecord.setGrunnopplysninger(baseProperties);
        medicalRecord.setMerknad(personalDataDTO.getMerknad());

        return medicalRecord;
    }
    
    public static PersondataDTO convertToPersonalDataDTO(Pasientjournal medicalRecord) {
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
    
    public static MedicalRecordDTO convertToMedicalRecordDTO(final Pasientjournal medicalRecord,
                                                             final TransferDTO transferDTO,
                                                             final String business) {
        final PersondataDTO personalData = convertToPersonalDataDTO(medicalRecord);
        final MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO(); 

        medicalRecordDTO.setPersondata(personalData);
        medicalRecordDTO.setAvleveringBeskrivelse(transferDTO.getTransferDescription());
        medicalRecordDTO.setAvleveringsidentifikator(transferDTO.getAgreementId());
        medicalRecordDTO.setAvleveringLaast(transferDTO.isLocked());
        
        final Set<Diagnose> diagnosisSet = medicalRecord.getDiagnose();
        final List<DiagnoseDTO> diagnoseDTOList = DiagnosisConverter.convertToDiagnosisDTOList(diagnosisSet);
        medicalRecordDTO.setDiagnoser(diagnoseDTOList);
        
        medicalRecordDTO.setVirksomhet(business);
        
        return medicalRecordDTO;
    }
    
    public static RecordTransferDTO convertToRecordTransferDTO(Pasientjournal medicalRecord) {
        RecordTransferDTO recordTransferDTO = new RecordTransferDTO();

        final Grunnopplysninger baseInformation = medicalRecord.getGrunnopplysninger();
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

        final Journalidentifikator journalId = medicalRecord.getJournalidentifikator();
        if (journalId != null) {
            recordTransferDTO.setJnr(journalId.getJournalnummer());
            recordTransferDTO.setLnr(journalId.getLøpenummer());
        }

        recordTransferDTO.setFanearkid(Integer.parseInt(medicalRecord.getFanearkid()));

        final List<Lagringsenhet> storageUnitList = medicalRecord.getLagringsenhet();
        if (storageUnitList != null && storageUnitList.size() > 0) {
            recordTransferDTO.setLagringsenhet(storageUnitList.get(0).getIdentifikator());
        }

        if (medicalRecord.getOppdateringsinfo() != null) {
            recordTransferDTO.setOppdatertAv(medicalRecord.getOppdateringsinfo().getOppdatertAv());

            if (medicalRecord.getOppdateringsinfo().getSistOppdatert() != null) {
                try {
                    recordTransferDTO.setOpprettetDato(medicalRecord.getOppdateringsinfo().getSistOppdatert().getTimeInMillis());
                } catch (Throwable ignored) {}
            } else {
                recordTransferDTO.setOpprettetDato(0L);
            }
        }

        recordTransferDTO.setUuid(medicalRecord.getUuid());

        return recordTransferDTO;
    }

    public static List<RecordTransferDTO> convertToRecordTransferDTOList(List<Pasientjournal> medicalRecordList) {
        return medicalRecordList.stream()
            .map(MedicalRecordConverter::convertToRecordTransferDTO)
            .collect(Collectors.toList());
    }
}