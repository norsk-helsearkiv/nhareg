package no.arkivverket.helsearkiv.nhareg.tjeneste;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.utilities.AdminHandler;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import no.arkivverket.helsearkiv.nhareg.utilities.UserHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class AvtaleTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private AvtaleTjeneste tjeneste;

    @Inject
    private UserHandler userHandler;
    
    @Inject
    private AdminHandler adminHandler;
    
    @Test
    public void getAvleveringer_gyldigId_skalReturnereEnAvtale() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Response response = tjeneste.getAvleveringer("Avtale1");
                
                assertEquals(200, response.getStatus());
        
                List<Avlevering> avleveringListe = (List<Avlevering>) response.getEntity();
                assertEquals(1, avleveringListe.size());
                
                return null;
            }
        });
    }

    @Test
    public void delete_ugyldigId_skalKasteNoResultException() throws Exception {
        adminHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                try {
                    tjeneste.delete("tull");
                } catch (EJBException e) {
                    assertEquals(NoResultException.class, e.getCause().getClass());
                }
                
                return null;
            }
        });
    }

    @Test(expected = ValideringsfeilException.class)
    public void delete_gyldigAvtaleMedBarn_skalKasteValderingsFeil() throws Exception {
        adminHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                tjeneste.delete("Avtale1");
                
                return null;
            }
        });
    }

    @Test
    public void delete_sletteAvtaleUtenBarn_skalReturnereSlettetAvtale() throws Exception {
        adminHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Avtale avtale = new Avtale();
                avtale.setAvtalebeskrivelse("beskrivelse");
                avtale.setAvtaleidentifikator("test-avtale");
        
                Calendar calendar = Calendar.getInstance();
                avtale.setAvtaledato(calendar);
        
                Virksomhet virksomhet = new Virksomhet();
                virksomhet.setOrganisasjonsnummer("100");
                virksomhet.setNavn("Testorganisasjon");
                avtale.setVirksomhet(virksomhet);
                
                Avtale nyAvtale = tjeneste.create(avtale);
                assertNotNull(nyAvtale);
        
                Avtale slettetAvtale = tjeneste.delete("test-avtale");
                assertNotNull(slettetAvtale);
                
                try {
                    tjeneste.getSingleInstance(slettetAvtale.getAvtaleidentifikator());
                } catch (EJBTransactionRolledbackException rbe) {
                    assertEquals(NoResultException.class, rbe.getCause().getClass());
                }
                
                return null;
            }
        });
    }
}
