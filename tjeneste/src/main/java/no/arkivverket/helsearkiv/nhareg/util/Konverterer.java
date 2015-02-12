package no.arkivverket.helsearkiv.nhareg.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Identifikator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Journalidentifikator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kontakt;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;

/**
 * Implementeres som Transformer
 * @author robing
 */
@Deprecated
public class Konverterer {

    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    public static Pasientjournal tilPasientjournal(PersondataDTO person) throws ParseException {
        Pasientjournal pasientjournal = new Pasientjournal();

        if (person.getUuid() != null) {
            pasientjournal.setUuid(person.getUuid());
        }

        if (person.getLagringsenheter() != null) {
            for (String enhet : person.getLagringsenheter()) {
                Lagringsenhet lEnhet = new Lagringsenhet();
                lEnhet.setIdentifikator(enhet);
                lEnhet.setUuid(UUID.randomUUID().toString());
                pasientjournal.getLagringsenhet().add(lEnhet);
            }
        }

        Journalidentifikator journalId = new Journalidentifikator();
        if (person.getJournalnummer() != null) {
            journalId.setJournalnummer(person.getJournalnummer());
        }
        if (person.getLopenummer() != null) {
            journalId.setLøpenummer(person.getLopenummer());
        }
        pasientjournal.setJournalidentifikator(journalId);

        Grunnopplysninger grunnopplysninger = new Grunnopplysninger();
        if (person.getFodselsnummer() != null) {
            Identifikator identifikator = new Identifikator();
            identifikator.setPID(person.getFodselsnummer());
            identifikator.setTypePID("fødelsnummer");
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

        Kontakt kontakt = new Kontakt();
        if (person.getfKontakt() != null) {
            kontakt.setFoerste(tilDatoEllerAar(person.getfKontakt()));
        }
        if (person.getsKontakt() != null) {
            kontakt.setSiste(tilDatoEllerAar(person.getsKontakt()));
        }
        grunnopplysninger.setKontakt(kontakt);

        pasientjournal.setGrunnopplysninger(grunnopplysninger);
        return pasientjournal;
    }

    public static PasientjournalSokeresultatDTO tilPasientjournalSokeresultatDTO(Pasientjournal pasientjournal) {
        PasientjournalSokeresultatDTO resultat = new PasientjournalSokeresultatDTO();

        if (pasientjournal.getGrunnopplysninger() != null) {
            resultat.setNavn(pasientjournal.getGrunnopplysninger().getPnavn());

            if (pasientjournal.getGrunnopplysninger().getIdentifikator() != null) {
                resultat.setFodselsnummer(pasientjournal.getGrunnopplysninger().getIdentifikator().getPID());
            }
        }

        resultat.setUuid(pasientjournal.getUuid());
        return resultat;
    }

    public static PasientjournalDTO tilPasientjournalDTO(Pasientjournal pasientjournal) {
        PasientjournalDTO dto = new PasientjournalDTO();
        PersondataDTO person = new PersondataDTO();
        person.setUuid(pasientjournal.getUuid());

        if (pasientjournal.getLagringsenhet() != null && !pasientjournal.getLagringsenhet().isEmpty()) {
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
        dto.setPersondata(person);

        //Diagnoser
        List<DiagnoseDTO> diagnoser = new ArrayList<DiagnoseDTO>();
        
        for(Diagnose d : pasientjournal.getDiagnose()) {
            DiagnoseDTO ddto = new DiagnoseDTO();
            ddto.setDiagnosedato(d.getDiagdato().toString());
            ddto.setDiagnosekode(d.getDiagnosekode().getCode());
            ddto.setDiagnosetekst(d.getDiagnosetekst());
            ddto.setUuid(d.getUuid());
            diagnoser.add(ddto);
        }
        dto.setDiagnoser(diagnoser);
        
        return dto;
    }

    static String tilString(DatoEllerAar dato) {
        if (dato != null) {
            if (dato.getDato() != null) {
                return format.format(dato.getDato().getTime());
            } else {
                return dato.getAar().toString();
            }
        }
        return null;
    }

    static DatoEllerAar tilDatoEllerAar(String tid) throws ParseException {
        if (tid.toLowerCase().equals("mors")) {
            return null;
        }

        DatoEllerAar dea = new DatoEllerAar();
        if (tid.length() == 4) {
            dea.setAar(Integer.parseInt(tid));
            return dea;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(format.parse(tid));

        dea.setDato(cal);
        return dea;
    }

}
