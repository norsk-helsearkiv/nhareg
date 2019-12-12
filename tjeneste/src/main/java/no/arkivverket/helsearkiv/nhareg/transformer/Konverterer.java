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