package no.arkivverket.helsearkiv.nhareg.configuration;

import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class ConfigurationDAOTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private ConfigurationDAO configurationDAO;

    @Test
    public void getValue_LowLim_shouldNotBeNull() {
        final String date = configurationDAO.getValue("LowLim");
        assertNotNull(date);
    }

    @Test
    public void getDate_LowLim_shouldNotBeNull() {
        final LocalDate date = configurationDAO.getDate("LowLim");
        assertNotNull(date);
    }

    @Test
    public void getInt_MaxAge_shouldBe200() {
        final Integer integer = configurationDAO.getInt("MaxAge");
        assertEquals(Integer.valueOf(200), integer);
    }
}