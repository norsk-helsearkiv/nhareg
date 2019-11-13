package no.arkivverket.helsearkiv.nhareg.tjeneste;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;

import static org.junit.Assert.assertNotNull;

import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import no.arkivverket.helsearkiv.nhareg.utilities.UserHandler;

@RunWith(Arquillian.class)
public class DiagnosekodeTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private DiagnosekodeTjeneste tjeneste;

    @Inject
    private UserHandler userHandler;
    
    @Test
    public void create_nyDiagnosekode_skalIkkeGiNull() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Diagnosekode diagnosekode = new Diagnosekode();
                diagnosekode.setCode("Diagnosekode100");
                diagnosekode.setCodeSystem("Kodesystem99");
                diagnosekode.setCodeSystemVersion("0.9.Beta");
                diagnosekode.setDisplayName("Influensa beta");
                Diagnosekode response = tjeneste.create(diagnosekode);
                assertNotNull(response);

                return null;
            }
        });
    }

    @Test
    public void getAll_utenPaginering_skalFinneTre() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
                List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
                assertNotNull(diagnosekoder);
                assertEquals(2, diagnosekoder.size());
                
                return null;
            }
        });
    }

    @Test
    public void getAll_medPaginering_skalFinneEn() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
                queryParameters.add(EntitetsTjeneste.SIDE, "1");
                queryParameters.add(EntitetsTjeneste.ANTALL, "1");
                List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
                assertNotNull(diagnosekoder);
                assertEquals(1, diagnosekoder.size());
                
                return null;
            }
        });
    }

    @Test
    public void hentDiagnosekoderMedCode_gyldigCode_skalFinneEn() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                String code = "Code0";
                List<Diagnosekode> diagnosekoder = tjeneste.hentDiagnosekoderMedCode(code);
                assertNotNull(diagnosekoder);
                assertEquals(1, diagnosekoder.size());
                
                return null;
            }
        });
    }

    @Test
    public void getAll_medCode_skalFinneEn() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                String code = "Code0";
                MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
                queryParameters.add(DiagnosekodeTjeneste.CODE_QUERY_PARAMETER, code);
                List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
                assertNotNull(diagnosekoder);
                assertEquals(1, diagnosekoder.size());

                return null;
            }
        });
    }

    @Test
    public void getAll_displayNameLike_skalFinneEn() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                String displayNameLike = "ode";
                MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
                queryParameters.add(DiagnosekodeTjeneste.DISPLAY_NAME_LIKE_QUERY_PARAMETER, displayNameLike);
                List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
                assertNotNull(diagnosekoder);
                assertEquals(1, diagnosekoder.size());

                return null;
            }
        });
    }

    @Test
    public void getAll_displayNameLikeIgnoreCase_skalFinneEn() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                String displayNameLike = "oDe";
                MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
                queryParameters.add(DiagnosekodeTjeneste.DISPLAY_NAME_LIKE_QUERY_PARAMETER, displayNameLike);
                List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
                assertNotNull(diagnosekoder);
                assertEquals(1, diagnosekoder.size());

                return null;
            }
        });
    }

    @Test
    public void hentDiagnosekoderMedCode_ukjentCode_skalFinneNull() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                String code = "Ukjent";
                List<Diagnosekode> diagnosekoder = tjeneste.hentDiagnosekoderMedCode(code);
                assertNotNull(diagnosekoder);
                assertEquals(0, diagnosekoder.size());

                return null;
            }
        });
    }

    @Test
    public void getAll_medUkjentCode_skalFinneNull() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                String code = "Ukjent";
                MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
                queryParameters.add("code", code);
                List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
                assertNotNull(diagnosekoder);
                assertEquals(0, diagnosekoder.size());

                return null;
            }
        });
    }

    @Test
    public void hentDiagnosekoderMedCode_nullCode_skalFinneNull() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                String code = null;
                List<Diagnosekode> diagnosekoder = tjeneste.hentDiagnosekoderMedCode(code);
                assertNotNull(diagnosekoder);
                assertEquals(0, diagnosekoder.size());

                return null;
            }
        });
    }

    @Test
    public void getAll_nullCode_skalFinneNull() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                String code = null;
                MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
                queryParameters.add("code", code);
                List<Diagnosekode> diagnosekoder = tjeneste.getAll(queryParameters);
                assertNotNull(diagnosekoder);
                assertEquals(0, diagnosekoder.size());

                return null;
            }
        });
    }
}
