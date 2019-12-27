package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(Arquillian.class)
public class StorageUnitServiceTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private StorageUnitServiceInterface storageUnitService;
    
    @Before
    public void setUp() {
        storageUnitService.setSessionContext(mock(SessionContext.class));
    }
    
    @Test
    public void create_missingId_shouldThrowValideringsfeilException() {
        final Lagringsenhet storageUnit = new Lagringsenhet();
        try {
            storageUnitService.create(storageUnit);
        } catch (EJBException ejb) {
            assertEquals(ejb.getCause().getClass(), ValideringsfeilException.class);
        }
    }

    @Test
    public void create_withValidId_shouldNotGetNull() {
        final Lagringsenhet storageUnit = new Lagringsenhet();
        storageUnit.setIdentifikator("LagringsenhetForEnhetstesting");
        final Lagringsenhet newStorageUnit = storageUnitService.create(storageUnit);
        assertNotNull(newStorageUnit);
    }

    @Test
    public void getById_validId_shouldReturnStorageUnit() {
        final Lagringsenhet lagringsenhet = storageUnitService.getById("boks1");
        assertNotNull(lagringsenhet);
    }

}