package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(Arquillian.class)
public class StorageUnitServiceTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private StorageUnitServiceInterface storageUnitService;
    
    @Test(expected = ValidationErrorException.class)
    public void create_missingId_shouldThrowValideringsfeilException() {
        final Lagringsenhet storageUnit = new Lagringsenhet();
        storageUnitService.create(storageUnit);
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
        final Lagringsenhet storageUnit = storageUnitService.getById("boks1");
        assertNotNull(storageUnit);
    }

    @Test
    public void getById_invalidId_shouldReturnNull() {
        final Lagringsenhet storageUnit = storageUnitService.getById("invalid");
        assertNull(storageUnit);
    }
    
}