package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;

import java.util.List;
import java.util.stream.Collectors;


public class MedicalRecordMapper {
    
    public MedicalRecordMapper() {}
    
    public PasientjournalSokeresultatDTO mapToSearchResultDTO(Pasientjournal pasientjournal) {
        PasientjournalSokeresultatDTO searchResultDTO = new PasientjournalSokeresultatDTO();

        final Grunnopplysninger baseInformation = pasientjournal.getGrunnopplysninger();
        if (baseInformation != null) {
            searchResultDTO.setNavn(baseInformation.getPnavn());

            if (baseInformation.getIdentifikator() != null) {
                searchResultDTO.setFodselsnummer(baseInformation.getIdentifikator().getPID());
            }

            final DatoEllerAar born = baseInformation.getFødt();
            if (born != null) {
                final String yearBorn = String.valueOf(born.getYear());
                searchResultDTO.setFaar(yearBorn);
            }

            if (baseInformation.getFodtdatoUkjent() != null &&
                baseInformation.getFodtdatoUkjent()) {
                searchResultDTO.setFaar("ukjent");
            }

            final DatoEllerAar dead = baseInformation.getDød();
            if (dead != null) {
                final String yearDied = String.valueOf(dead.getYear());
                searchResultDTO.setDaar(yearDied);
            }

            if (baseInformation.getDødsdatoUkjent() != null &&
                baseInformation.getDødsdatoUkjent()) {
                searchResultDTO.setDaar("mors");
            }
        }

        final Journalidentifikator journalId = pasientjournal.getJournalidentifikator();
        if (journalId != null) {
            searchResultDTO.setJnr(journalId.getJournalnummer());
            searchResultDTO.setLnr(journalId.getLøpenummer());
        }

        searchResultDTO.setFanearkid(pasientjournal.getFanearkid());

        final List<Lagringsenhet> storageUnitList = pasientjournal.getLagringsenhet();
        if (storageUnitList != null && storageUnitList.size() > 0) {
            searchResultDTO.setLagringsenhet(storageUnitList.get(0).getIdentifikator());
        }

        if (pasientjournal.getOppdateringsinfo() != null) {
            searchResultDTO.setOppdatertAv(pasientjournal.getOppdateringsinfo().getOppdatertAv());

            if (pasientjournal.getOppdateringsinfo().getSistOppdatert() != null) {
                try {
                    searchResultDTO.setOpprettetDato(pasientjournal.getOppdateringsinfo().getSistOppdatert().getTimeInMillis());
                } catch (Throwable ignored) {}
            } else {
                searchResultDTO.setOpprettetDato(0L);
            }
        }

        searchResultDTO.setUuid(pasientjournal.getUuid());

        return searchResultDTO;
    }

    public List<PasientjournalSokeresultatDTO> mapToSearchResultDTOList(List<Pasientjournal> pasientjournalList) {
        return pasientjournalList.stream()
            .map(this::mapToSearchResultDTO)
            .collect(Collectors.toList());
    }
}
