package no.arkivverket.helsearkiv.nhareg.tjeneste;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;

import static org.junit.Assert.assertNotNull;

import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;

@RunWith(Arquillian.class)
public class AvtaleTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private AvtaleTjeneste tjeneste;

    @Test
    public void getAvleveringer_henterAvleveringerForAvtale_200() {
        Response rsp = tjeneste.getAvleveringer("Avtale1");
        assertEquals(200, rsp.getStatus());

        List<Avlevering> avleveriner = (List<Avlevering>) rsp.getEntity();
        assertEquals(1, avleveriner.size());
    }

    @Test
    public void delete_sletteEnSomIkkeFinnes_404() {
        try {
            tjeneste.delete("tull");
        } catch (EJBException e) {
            assertEquals(NoResultException.class, e.getCause().getClass());
        }
    }

    @Test(expected = ValideringsfeilException.class)
    public void delete_sletteEnMedAvleveringer_409() {
        tjeneste.delete("Avtale1");
    }

    @Test
    public void delete_sletteEnUtenAvleveringer_200() {
        Avtale a1 = new Avtale();
        a1.setAvtalebeskrivelse("beskrivelse");
        a1.setAvtaleidentifikator("test-avtale");

        Calendar dag = Calendar.getInstance();
        a1.setAvtaledato(dag);

        Virksomhet v = new Virksomhet();
        v.setOrganisasjonsnummer("100");
        v.setNavn("Testorganisasjon");
        a1.setVirksomhet(v);

        Avtale ny = tjeneste.create(a1);
        assertNotNull(ny);

        Avtale rsp = tjeneste.delete("test-avtale");
        assertNotNull(rsp);
    }
}
