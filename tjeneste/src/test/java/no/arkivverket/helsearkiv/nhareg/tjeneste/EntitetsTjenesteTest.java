package no.arkivverket.helsearkiv.nhareg.tjeneste;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.MultivaluedHashMap;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;
import no.arkivverket.helsearkiv.nhareg.utilities.MockUriInfo;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import no.arkivverket.helsearkiv.nhareg.utilities.UserHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tester grunn funksjonaliteten til Entitetstjeneste, basert på det aller
 * enkleste objektet, Avtale
 *
 * @author robing
 */
@RunWith(Arquillian.class)
public class EntitetsTjenesteTest {

    @Inject
    private EntitetsTjenesteMock tjeneste;

    @Inject
    private UserHandler userHandler;
    
    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Test
    public void getAll_tomMap_skalIkkeGiTomListe() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                List<Avtale> avtaler = tjeneste.getAll(new MultivaluedHashMap<String, String>());
                assertFalse(avtaler.isEmpty());
                
                return null;
            }
        });
    }

    @Test
    public void getCount_nyMap_skalGiAntallStorreEnnNull() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Map<String, Long> map = tjeneste.getCount(new MockUriInfo());
                assertTrue(map.get("antall") > 0);
                
                return null;
            }
        });
    }

    @Test
    public void getSingleInstance_ugyldigInstance_skalGiNoResultException() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                try {
                    tjeneste.getSingleInstance("tull");
                } catch (EJBTransactionRolledbackException ex) {
                    assertEquals(NoResultException.class, ex.getCause().getClass());
                }
                
                return null;
            }
        });
    }

    @Test
    public void getSingleInstance_gyldigInstance_skalReturnereAvtale() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Avtale avtale = tjeneste.getSingleInstance("Avtale1");
                assertNotNull(avtale);

                return null;
            }
        });
    }

    @Test
    public void create_nyAvtale_skalReturnereAvtale() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Avtale avtale = tjeneste.create(getAvtale());
                assertNotNull(avtale);
                
                return null;
            }
        });
    }

    @Test
    public void update_oppdaterBeskrivelse_skalReturnereOppdatertAvtale() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                final String beskrivelse = "ny beskrivelse";
                Avtale avtale = tjeneste.getSingleInstance("Avtale1");
                avtale.setAvtalebeskrivelse(beskrivelse);
                Avtale oppdatertAvtale = tjeneste.update(avtale);
                
                assertNotNull(oppdatertAvtale);
                assertEquals(beskrivelse, oppdatertAvtale.getAvtalebeskrivelse());
                
                return null;
            }
        });
    }

    @Test
    public void delete_ugyldigId_skalKasteNoResultException() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                try {
                    tjeneste.delete("tull");
                } catch(EJBException e) {
                    assertEquals(NoResultException.class, e.getCause().getClass());
                }
                
                return null;
            }
        });
    }

    @Test
    public void delete_gyldigId_skalReturnereSlettetAvtale() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                final Avtale avtale = getAvtale();
                tjeneste.create(avtale);
                Avtale slettetAvtale = tjeneste.delete(avtale.getAvtaleidentifikator());
        
                assertNotNull(slettetAvtale);

                try {
                    tjeneste.getSingleInstance(avtale.getAvtaleidentifikator());
                } catch (EJBTransactionRolledbackException ex) {
                    assertEquals(NoResultException.class, ex.getCause().getClass());
                }
                
                return null;
            }
        });
    }

    private Avtale getAvtale() {
        Avtale a1 = new Avtale();
        a1.setAvtalebeskrivelse("beskrivelse");
        a1.setAvtaleidentifikator("test-avtale");

        Calendar dag = Calendar.getInstance();
        a1.setAvtaledato(dag);

        Virksomhet v = new Virksomhet();
        v.setOrganisasjonsnummer("100");
        v.setNavn("Testorganisasjon");
        a1.setVirksomhet(v);
        return a1;
    }
}
