package no.arkivverket.helsearkiv.nhareg.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by haraldk on 13.05.15.
 */
public class PersonnummerValidererTest {

    @Test
    public void testRiktigFnr()  {
        assertTrue(PersonnummerValiderer.gyldigFnr("14037243710"));
    }
    @Test
    public void testRiktigDnr()  {
        assertTrue(PersonnummerValiderer.gyldigFnr("64060038145"));
    }
    @Test
    public void testRiktigHnr() {
        assertTrue(PersonnummerValiderer.gyldigFnr("19410542709"));
    }

    @Test
    public void testFeilFnr()  {
        assertFalse(PersonnummerValiderer.gyldigFnr("14037243711"));
    }
    @Test
    public void testFeilDnr()  {
        assertFalse(PersonnummerValiderer.gyldigFnr("64060038144"));
    }
    @Test
    public void testFeilHnr()  {
        assertFalse(PersonnummerValiderer.gyldigFnr("230881"));
    }
}