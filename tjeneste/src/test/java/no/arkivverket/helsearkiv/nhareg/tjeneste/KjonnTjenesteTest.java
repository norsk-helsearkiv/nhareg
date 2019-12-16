package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;
import no.arkivverket.helsearkiv.nhareg.gender.GenderDAO;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

public class KjonnTjenesteTest {

    @Inject
    private GenderDAO genderDAO;

    @Test
    public void create_gyldigKjonn_skalIkkeGiNull() {
        final Kjønn kjonn = new Kjønn();
        kjonn.setCode("I");
        kjonn.setDisplayName("Intetkjønn");
        final Kjønn result = genderDAO.create(kjonn);
        assertNotNull(result);
    }
}