package no.arkivverket.helsearkiv.nhareg.util;

import java.text.ParseException;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Identifikator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Journalidentifikator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kontakt;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author robing
 */

@RunWith(Arquillian.class)
public class KonvertererTest {
    
    @Test
    public void tilPasientjournalTest() throws ParseException {
        PersondataDTO dto = new PersondataDTO();
        dto.setDod("2009");
        dto.setFodselsnummer("01010942345");
        dto.setFodt("2009");
        dto.setJournalnummer("123");
        dto.setKjonn("K");
        String[] lagringsenheter = { "boks1" };
        dto.setLagringsenheter(lagringsenheter);
        dto.setLopenummer("234");
        dto.setNavn("Natalie");
        dto.setUuid("uuid1");
        dto.setfKontakt("2009");
        dto.setsKontakt("2009");
        
        Pasientjournal pasientjournal = Konverterer.tilPasientjournal(dto);
        
        assertEquals(dto.getFodselsnummer(), pasientjournal.getGrunnopplysninger().getIdentifikator().getPID());
        int dod = pasientjournal.getGrunnopplysninger().getDød().getAar();
        assertEquals(2009, dod);
    }
    
    @Test
    public void tilPasientjournalSokeresultatDTOTest() {
        PasientjournalSokeresultatDTO dto = Konverterer.tilPasientjournalSokeresultatDTO(getPasientjournal());
        
        assertEquals("Natalie", dto.getNavn());
        assertEquals("01010942345", dto.getFodselsnummer());
        assertEquals("uuid1", dto.getUuid());
    }
    
    @Test
    public void tilPasientjournalDTOTest() {
        PasientjournalDTO dto = Konverterer.tilPasientjournalDTO(getPasientjournal());
        assertEquals("01010942345", dto.getPersondata().getFodselsnummer());
        assertEquals("Natalie", dto.getPersondata().getNavn());
    }
    
    private Pasientjournal getPasientjournal() {
        Pasientjournal pasientjournal = new Pasientjournal();
        
        Grunnopplysninger grn = new Grunnopplysninger();
        Identifikator id = new Identifikator();
        id.setPID("01010942345");
        id.setTypePID("fodselsnummer");
        grn.setIdentifikator(id);
        
        grn.setPnavn("Natalie");
        grn.setDød(getDato());
        grn.setDødsdatoUkjent(Boolean.FALSE);
        grn.setFødt(getDato());
        
        Kjønn k = new Kjønn();
        k.setCode("K");
        k.setDisplayName("Kvinne");
        grn.setKjønn(k);
        
        Kontakt kontakt = new Kontakt();
        kontakt.setFoerste(getDato());
        kontakt.setSiste(getDato());
        grn.setKontakt(kontakt);
        pasientjournal.setGrunnopplysninger(grn);

        Journalidentifikator identifikator = new Journalidentifikator();
        identifikator.setJournalnummer("123");
        identifikator.setLøpenummer("234");
        pasientjournal.setJournalidentifikator(identifikator);
        
        pasientjournal.setUuid("uuid1");
        return pasientjournal;
    }
    
    private DatoEllerAar getDato() {
        DatoEllerAar dato = new DatoEllerAar();
        dato.setAar(2000);
        return dato;
    }
}


