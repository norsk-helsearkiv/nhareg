package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.StorageUnitDTO;
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
        final StorageUnit storageUnit = new StorageUnit();
        storageUnitService.create(storageUnit);
    }

    @Test
    public void create_withValidId_shouldNotGetNull() {
        final StorageUnit storageUnit = new StorageUnit();
        storageUnit.setId("LagringsenhetForEnhetstesting");
        final StorageUnitDTO storageUnitDTO = storageUnitService.create(storageUnit);
        assertNotNull(storageUnitDTO);
    }

    @Test
    public void getById_validUUID_shouldReturnStorageUnit() {
        final StorageUnitDTO storageUnitDTO = storageUnitService.getById("enhet-1");
        assertNotNull(storageUnitDTO);
    }

    @Test
    public void getById_invalidUUID_shouldReturnNull() {
        final StorageUnitDTO storageUnitDTO = storageUnitService.getById("invalid");
        assertNull(storageUnitDTO);
    }
    
    @Test
    public void getByIdentifier_validId_shouldReturnStorageUnit() {
        final StorageUnitDTO storageUnitDTO = storageUnitService.getByIdentifier("boks-1");
        assertNotNull(storageUnitDTO);
    }
    
    @Test
    public void getByIdentifier_invalidId_shouldReturnNull() {
        final StorageUnitDTO storageUnitDTO = storageUnitService.getByIdentifier("invalid");
        assertNull(storageUnitDTO);
    }
    
}