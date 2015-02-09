package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.Calendar;
import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
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
    public void getAvleveringer_fantIngenAvtale_404() {
        Response rsp = tjeneste.getAvleveringer("tull");
        assertEquals(404, rsp.getStatus());
    }
    
    @Test
    public void delete_sletteEnSomIkkeFinnes_404() {
        Response rsp = tjeneste.delete("tull");
        assertEquals(404, rsp.getStatus());
    }
    
    @Test
    public void delete_sletteEnMedAvleveringer_409() {
        Response rsp = tjeneste.delete("Avtale1");
        assertEquals(409, rsp.getStatus());
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
        
        Response ny = tjeneste.create(a1);
        assertEquals(200, ny.getStatus());
        
        Response rsp = tjeneste.delete("test-avtale");
        assertEquals(200, rsp.getStatus());
    }
}
