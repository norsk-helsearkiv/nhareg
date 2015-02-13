package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.util.RESTDeployment;
import java.util.Calendar;
import javax.ejb.EJBException;
import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    }
    
    @Test
    public void delete_sletteEnSomIkkeFinnes_404() {
        try {
            tjeneste.delete("tull");
        } catch(EJBException e) {
            assertEquals(NoResultException.class, e.getCause().getClass());
        }
    }
    
    @Test
    public void delete_sletteEnMedAvleveringer_409() {
        try {
            tjeneste.delete("Avtale1");
        } catch(EJBException e) {
            assertEquals(ValideringsfeilException.class, e.getCause().getClass());
        }
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
