package no.arkivverket.helsearkiv.nhareg.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Identifikator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Journalidentifikator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kontakt;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;

/**
 *
 * @author robing
 */
public class Konverterer {
    
    public static Pasientjournal fraDTO(PersondataDTO person) throws ParseException {
        Pasientjournal pasientjournal = new Pasientjournal();
        
        if(person.getUuid() != null) {
            pasientjournal.setUuid(person.getUuid());
        }
        
        if(person.getLagringsenheter() != null) {
            for(String enhet : person.getLagringsenheter()) {
                Lagringsenhet lEnhet = new Lagringsenhet();
                lEnhet.setIdentifikator(enhet);
                lEnhet.setUuid(UUID.randomUUID().toString());
                pasientjournal.getLagringsenhet().add(lEnhet);
            }
        }
        
        Journalidentifikator journalId = new Journalidentifikator();
        if(person.getJournalnummer() != null) {
            journalId.setJournalnummer(person.getJournalnummer());
        }
        if(person.getLopenummer() != null) {
            journalId.setLøpenummer(person.getLopenummer());
        }
        pasientjournal.setJournalidentifikator(journalId);
        
        Grunnopplysninger grunnopplysninger = new Grunnopplysninger();
        if(person.getFodselsnummer() != null) {
            Identifikator identifikator = new Identifikator();
            identifikator.setPID(person.getFodselsnummer());
            identifikator.setTypePID("fødelsnummer");
            grunnopplysninger.setIdentifikator(identifikator);
        }
        if(person.getNavn() != null) {
            grunnopplysninger.setPnavn(person.getNavn());
        }
        if(person.getKjonn() != null) {
            Kjønn k = new Kjønn();
            k.setCode(person.getKjonn());
            grunnopplysninger.setKjønn(k);
        }
        if(person.getFodt() != null) {
            grunnopplysninger.setFødt(tilDatoEllerAar(person.getFodt()));
        }
        if(person.getDod() != null) {
            grunnopplysninger.setDød(tilDatoEllerAar(person.getDod()));
        }
        grunnopplysninger.setDødsdatoUkjent(grunnopplysninger.getDød() == null);
        
        Kontakt kontakt = new Kontakt();
        if(person.getfKontakt() != null) {
            kontakt.setFoerste(tilDatoEllerAar(person.getfKontakt()));
        }
        if(person.getsKontakt() != null) {
            kontakt.setSiste(tilDatoEllerAar(person.getsKontakt()));
        }
        grunnopplysninger.setKontakt(kontakt);
        
        pasientjournal.setGrunnopplysninger(grunnopplysninger);
        return pasientjournal;
    }
    
    public static PasientjournalSokeresultatDTO tilDTO(Pasientjournal pasientjournal) {
        PasientjournalSokeresultatDTO resultat = new PasientjournalSokeresultatDTO();
        
        if(pasientjournal.getGrunnopplysninger() != null) {
            resultat.setNavn(pasientjournal.getGrunnopplysninger().getPnavn());
            
            if(pasientjournal.getGrunnopplysninger().getIdentifikator() != null) {
                resultat.setFodselsnummer(pasientjournal.getGrunnopplysninger().getIdentifikator().getPID());
            }
        }
        
        resultat.setUuid(pasientjournal.getUuid());
        return resultat;
    }
    
    private static DatoEllerAar tilDatoEllerAar(String tid) throws ParseException {
        if(tid.toLowerCase().equals("mors")) {
            return null;
        }
        
        DatoEllerAar dea = new DatoEllerAar();
        if(tid.length() == 4) {
            dea.setAar(Integer.parseInt(tid));
            return dea;
        }
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
        cal.setTime(sdf.parse(tid));
        
        dea.setDato(cal);
        return dea;
    }
    
}
