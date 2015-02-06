package no.arkivverket.helsearkiv.nhareg.tjeneste;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DiagnosekodeTjenesteTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private DiagnosekodeTjeneste tjeneste;

    @Test
    public void create() {
        Diagnosekode diagnosekode = new Diagnosekode();
        diagnosekode.setCode("Diagnosekode100");
        diagnosekode.setCodeSystem("Kodesystem99");
        diagnosekode.setCodeSystemVersion("0.9.Beta");
        diagnosekode.setDisplayName("Influensa beta");
        Response response = tjeneste.create(diagnosekode);
        assertNotNull(response);
    }
}
