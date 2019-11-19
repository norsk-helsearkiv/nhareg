package no.arkivverket.helsearkiv.nhareg.transformer;

import java.text.ParseException;
import java.util.Calendar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Identifikator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Journalidentifikator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kontakt;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author robing
 */
public class KonvertererTransformerTest {

    @Test
    public void tilPasientjournalTest() throws ParseException {
        PersondataDTO dto = new PersondataDTO();
        dto.setDod("2009");
        dto.setFodselsnummer("01010942345");
        dto.setFodt("2009");
        dto.setJournalnummer("123");
        dto.setKjonn("K");
        String[] lagringsenheter = {"boks1"};
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
        assertEquals(1, dto.getPersondata().getLagringsenheter().length);
        assertEquals("01010942345", dto.getPersondata().getFodselsnummer());
        assertEquals("Natalie", dto.getPersondata().getNavn());
    }

    @Test
    public void tilDatoEllerAar() throws ParseException {
        DatoEllerAar datoEllerAar = Konverterer.tilDatoEllerAar("15.03.2015");
        assertNotNull(datoEllerAar);
        assertNotNull(datoEllerAar.getDato());
        assertEquals(15, datoEllerAar.getDato().get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.MARCH, datoEllerAar.getDato().get(Calendar.MONTH));
        assertEquals(2015, datoEllerAar.getDato().get(Calendar.YEAR));
    }
    
    @Test
    public void tilString() throws ParseException {
        String datoString = "15.03.2015";
        DatoEllerAar datoEllerAar = Konverterer.tilDatoEllerAar(datoString);
        assertNotNull(datoEllerAar);
        assertNotNull(datoEllerAar.getDato());
        assertEquals(15, datoEllerAar.getDato().get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.MARCH, datoEllerAar.getDato().get(Calendar.MONTH));
        assertEquals(2015, datoEllerAar.getDato().get(Calendar.YEAR));
        //
        assertEquals(datoString,Konverterer.tilString(datoEllerAar));
    }

    private Pasientjournal getPasientjournal() {
        Pasientjournal pasientjournal = new Pasientjournal();

        Lagringsenhet lght = new Lagringsenhet();
        lght.setIdentifikator("Boks1");
        lght.setUuid("lagring-1-boks-1");
        pasientjournal.getLagringsenhet().add(lght);

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
