package no.arkivverket.helsearkiv.nhareg.transformer;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;
import no.arkivverket.helsearkiv.nhareg.util.PersonnummerValiderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Implementeres som Transformer
 *
 * @author robing
 */
@Deprecated
public class Konverterer {

    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    public static Pasientjournal tilPasientjournal(MedicalRecordDTO medicalRecordDTO) throws ParseException {
        return tilPasientjournal(medicalRecordDTO.getPersondata());
    }

    public static Pasientjournal tilPasientjournal(PersondataDTO person) throws ParseException {
        Pasientjournal pasientjournal = new Pasientjournal();

        if (person.getUuid() != null) {
            pasientjournal.setUuid(person.getUuid());
        }

        if (person.getLagringsenheter() != null) {
            for (String enhet : person.getLagringsenheter()) {
                Lagringsenhet lagringsenhet = new Lagringsenhet();
                lagringsenhet.setIdentifikator(enhet);
                lagringsenhet.setUuid(UUID.randomUUID().toString());
                pasientjournal.getLagringsenhet().add(lagringsenhet);
            }
        }

        Journalidentifikator journalId = new Journalidentifikator();

        if (person.getJournalnummer() != null) {
            journalId.setJournalnummer(person.getJournalnummer());
        }

        if (person.getLopenummer() != null) {
            journalId.setLøpenummer(person.getLopenummer());
        }

        if (person.getFanearkid()!=null){
            pasientjournal.setFanearkid(person.getFanearkid());
        }

        pasientjournal.setJournalidentifikator(journalId);

        Grunnopplysninger grunnopplysninger = new Grunnopplysninger();
        String fnr = person.getFodselsnummer();
        if (fnr != null) {
            Identifikator identifikator = new Identifikator();
            identifikator.setPID(fnr);

            if (PersonnummerValiderer.isHnummer(fnr)) {
                identifikator.setTypePID("H");
            } else if (PersonnummerValiderer.isDnummer(fnr)) {
                identifikator.setTypePID("D");
            } else if(PersonnummerValiderer.isFnummer(fnr)) {
                identifikator.setTypePID("F");
            }
            grunnopplysninger.setIdentifikator(identifikator);
        }

        if (person.getNavn() != null) {
            grunnopplysninger.setPnavn(person.getNavn());
        }

        if (person.getKjonn() != null) {
            Kjønn k = new Kjønn();
            k.setCode(person.getKjonn());
            grunnopplysninger.setKjønn(k);
        }

        if (person.getFodt() != null) {
            grunnopplysninger.setFødt(tilDatoEllerAar(person.getFodt()));
        }

        if (person.getDod() != null) {
            grunnopplysninger.setDød(tilDatoEllerAar(person.getDod()));
        }

        grunnopplysninger.setDødsdatoUkjent(grunnopplysninger.getDød() == null);
        grunnopplysninger.setFodtdatoUkjent(grunnopplysninger.getFødt() == null);

        Kontakt kontakt = new Kontakt();
        if (person.getFKontakt() != null) {
            kontakt.setFoerste(tilDatoEllerAar(person.getFKontakt()));
        }

        if (person.getSKontakt() != null) {
            kontakt.setSiste(tilDatoEllerAar(person.getSKontakt()));
        }

        grunnopplysninger.setKontakt(kontakt);
        pasientjournal.setGrunnopplysninger(grunnopplysninger);
        pasientjournal.setMerknad(person.getMerknad());

        return pasientjournal;
    }

    public static PasientjournalSokeresultatDTO tilPasientjournalSokeresultatDTO(Pasientjournal pasientjournal) {
        PasientjournalSokeresultatDTO resultat = new PasientjournalSokeresultatDTO();

        if (pasientjournal.getGrunnopplysninger() != null) {
            resultat.setNavn(pasientjournal.getGrunnopplysninger().getPnavn());

            if (pasientjournal.getGrunnopplysninger().getIdentifikator() != null) {
                resultat.setFodselsnummer(pasientjournal.getGrunnopplysninger().getIdentifikator().getPID());
            }

            DatoEllerAarTilStringTransformer trans = new DatoEllerAarTilStringTransformer();
            DatoEllerAar fodt = pasientjournal.getGrunnopplysninger().getFødt();
            if (fodt != null) {
                resultat.setFaar(trans.transform(fodt));
            }

            if (pasientjournal.getGrunnopplysninger().getFodtdatoUkjent() != null &&
                pasientjournal.getGrunnopplysninger().getFodtdatoUkjent()) {
                resultat.setFaar("ukjent");
            }

            DatoEllerAar dod = pasientjournal.getGrunnopplysninger().getDød();
            if (dod != null) {
                resultat.setDaar(trans.transform(dod));
            }
            if (pasientjournal.getGrunnopplysninger().getDødsdatoUkjent() != null &&
                pasientjournal.getGrunnopplysninger().getDødsdatoUkjent()) {
                resultat.setDaar("mors");
            }
        }

        if (pasientjournal.getJournalidentifikator() != null) {
            resultat.setJnr(pasientjournal.getJournalidentifikator().getJournalnummer());
            resultat.setLnr(pasientjournal.getJournalidentifikator().getLøpenummer());
        }

        resultat.setFanearkid(pasientjournal.getFanearkid());

        if (pasientjournal.getLagringsenhet() != null &&
            pasientjournal.getLagringsenhet().size() > 0) {
            resultat.setLagringsenhet(pasientjournal.getLagringsenhet().get(0).getIdentifikator());
        }

        if (pasientjournal.getOppdateringsinfo() != null) {
            resultat.setOppdatertAv(pasientjournal.getOppdateringsinfo().getOppdatertAv());
        }

        if (pasientjournal.getOppdateringsinfo() != null) {
            if (pasientjournal.getOppdateringsinfo().getSistOppdatert() != null) {
                try {
                    resultat.setOpprettetDato(pasientjournal.getOppdateringsinfo().getSistOppdatert().getTimeInMillis());
                } catch (Throwable ignored) {}
            } else {
                resultat.setOpprettetDato(0L);
            }
        }

        resultat.setUuid(pasientjournal.getUuid());
        
        return resultat;
    }

    public static PasientjournalDTO tilPasientjournalDTO(Pasientjournal pasientjournal) {
        PasientjournalDTO dto = new PasientjournalDTO();
        PersondataDTO person = new PersondataDTO();
        person.setUuid(pasientjournal.getUuid());
        person.setMerknad(pasientjournal.getMerknad());
        
        if (pasientjournal.getLagringsenhet() != null &&
            !pasientjournal.getLagringsenhet().isEmpty()) {
            String[] enheter = new String[pasientjournal.getLagringsenhet().size()];

            for (int i = 0; i < pasientjournal.getLagringsenhet().size(); i++) {
                enheter[i] = pasientjournal.getLagringsenhet().get(i).getIdentifikator();
            }

            person.setLagringsenheter(enheter);
        }

        if (pasientjournal.getGrunnopplysninger() != null) {
            Grunnopplysninger grn = pasientjournal.getGrunnopplysninger();
            person.setNavn(pasientjournal.getGrunnopplysninger().getPnavn());
            
            if (grn.getIdentifikator() != null) {
                person.setFodselsnummer(pasientjournal.getGrunnopplysninger().getIdentifikator().getPID());
            }

            if (grn.getKjønn() != null) {
                person.setKjonn(pasientjournal.getGrunnopplysninger().getKjønn().getCode());
            }

            if (grn.getFødt() != null) {
                person.setFodt(tilString(pasientjournal.getGrunnopplysninger().getFødt()));
            }
            
            if (grn.getDød() != null) {
                person.setDod(tilString(pasientjournal.getGrunnopplysninger().getDød()));
            }
            
            if (grn.getDødsdatoUkjent() != null && grn.getDødsdatoUkjent()) {
                person.setDod("mors");
            }
            
            if (grn.getFodtdatoUkjent() != null && grn.getFodtdatoUkjent()) {
                person.setFodt("ukjent");
            }
            
            if (grn.getKontakt() != null) {
                
                if (grn.getKontakt().getFoerste() != null) {
                    person.setfKontakt(tilString(grn.getKontakt().getFoerste()));
                }
                
                if (grn.getKontakt().getSiste() != null) {
                    person.setsKontakt(tilString(grn.getKontakt().getSiste()));
                }
            }
        }

        if (pasientjournal.getJournalidentifikator() != null) {
            person.setLopenummer(pasientjournal.getJournalidentifikator().getLøpenummer());
            person.setJournalnummer(pasientjournal.getJournalidentifikator().getJournalnummer());
        }
        
        person.setFanearkid(pasientjournal.getFanearkid());
        dto.setPersondata(person);

        return dto;
    }

    public static String tilString(DatoEllerAar dato) {
        if (dato != null) {
            if (dato.getDato() != null) {
                return format.format(dato.getDato().getTime());
            } else {
                return dato.getAar().toString();
            }
        }
        
        return null;
    }

    public static DatoEllerAar tilDatoEllerAar(String tid) throws ParseException {
        if (tid.toLowerCase().equals("mors") ||
                tid.isEmpty() ||
                tid.toLowerCase().equals("m") ||
                tid.toLowerCase().equals("ukjent") ||
                tid.toLowerCase().equals("u")) {
            return null;
        }

        DatoEllerAar datoEllerAar = new DatoEllerAar();
        if (tid.length() == 4) {
            datoEllerAar.setAar(Integer.parseInt(tid));
            return datoEllerAar;
        }

        Date dato = GyldigeDatoformater.getDate(tid);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dato);
        datoEllerAar.setDato(cal);

        return datoEllerAar;
    }
}
