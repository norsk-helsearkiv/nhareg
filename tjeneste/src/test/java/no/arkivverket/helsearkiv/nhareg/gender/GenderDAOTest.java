package no.arkivverket.helsearkiv.nhareg.gender;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.Gender;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class GenderDAOTest {
    
    @Deployment
    public static WebArchive createDeployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private GenderDAO genderDAO;
    
    @Test
    public void create_newGender_shouldReturnNewGender() {
        final Gender gender = new Gender();
        gender.setCode("I");
        gender.setDisplayName("Intenkjønn");
        
        final Gender newGender = genderDAO.create(gender);
        assertNotNull(newGender);
        assertEquals(newGender, gender);
    }
    
    @Test
    public void fetchById_validId_shouldReturnGender() {
        final Gender gender = genderDAO.fetchById("M");
        assertNotNull(gender);
        assertEquals("M", gender.getCode());
        assertEquals("Mann", gender.getDisplayName());
    }
    
}