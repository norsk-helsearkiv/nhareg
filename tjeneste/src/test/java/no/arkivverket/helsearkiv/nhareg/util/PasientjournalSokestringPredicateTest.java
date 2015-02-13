package no.arkivverket.helsearkiv.nhareg.util;

import java.util.ArrayList;
import java.util.List;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Identifikator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arnfinns
 */
public class PasientjournalSokestringPredicateTest {
    
    public PasientjournalSokestringPredicateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of evaluate method, of class PasientjournalSokestringPredicate.
     */
    @Test
    public void testEvaluate() {
        Identifikator i = new Identifikator();
        i.setPID("010261");
        Grunnopplysninger g = new Grunnopplysninger();
        g.setPnavn("Arnfinn Sandnes");
        Pasientjournal pasientjournal = new Pasientjournal();
        pasientjournal.setGrunnopplysninger(g);
        g.setIdentifikator(i);
        //
        List<String> sok = new ArrayList<String>();
        PasientjournalSokestringPredicate instance = new PasientjournalSokestringPredicate(sok);
        //
        assertFalse(instance.evaluate(pasientjournal));
        //
        sok.add("finn");
        assertTrue(instance.evaluate(pasientjournal));
        sok.add("tull");
        assertTrue(instance.evaluate(pasientjournal));
        //
        sok.clear();
        sok.add("tull");
        assertFalse(instance.evaluate(pasientjournal));
        //
        // Identifikator
        //
        sok.clear();
        sok.add("02");
        assertTrue(instance.evaluate(pasientjournal));
        sok.add("99");
        assertTrue(instance.evaluate(pasientjournal));
        
        sok.clear();
        sok.add("99");
        assertFalse(instance.evaluate(pasientjournal));
        
    }
    
}
