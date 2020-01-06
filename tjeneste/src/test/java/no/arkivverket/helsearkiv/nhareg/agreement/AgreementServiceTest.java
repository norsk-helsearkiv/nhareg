package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.MultivaluedHashMap;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class AgreementServiceTest {
    
    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private AgreementServiceInterface agreementService;

    @Test
    public void create_missingBusiness_shouldReturnAgreement() {
        final Agreement agreement = new Agreement("enhet-4", Calendar.getInstance(), "boks4", null);
        
        final Agreement newAgreement = agreementService.create(agreement);
        assertNotNull(newAgreement);
        assertEquals(newAgreement, agreement);
    }
    
    @Test
    public void getAll_emptyQuery_shouldReturnTwo() {
        final List<Agreement> agreementList = agreementService.getAll(new MultivaluedHashMap<>());
        assertEquals(2, agreementList.size());
    }

    @Test(expected = NoResultException.class)
    public void delete_invalidId_shouldThrowNoResultException() {
        try {
            agreementService.delete("tull");
        } catch (EJBException ejb) {
            if (ejb.getCause().getClass() == NoResultException.class) {
                throw new NoResultException();
            }
            
            throw ejb;
        }
    }

    @Test(expected = ValidationErrorException.class)
    public void delete_validIdWithChildren_shouldThrowValidationErrorException() {
        agreementService.delete("Avtale1");
    }

    @Test
    public void delete_validIdWithoutChildren_shouldReturnAgreement() {
        final Agreement transfer = new Agreement();
        final Calendar calendar = Calendar.getInstance();
        final Virksomhet virksomhet = new Virksomhet();
        final String agreementId = "test-avtale";

        transfer.setAvtalebeskrivelse("beskrivelse");
        transfer.setAgreementId(agreementId);
        transfer.setAvtaledato(calendar);

        virksomhet.setOrganisasjonsnummer("100");
        virksomhet.setNavn("Testorganisasjon");
        transfer.setVirksomhet(virksomhet);
        
        final Agreement newAgreement = agreementService.create(transfer);
        assertNotNull(newAgreement);

        final Agreement deletedAgreement = agreementService.delete(agreementId);
        assertNotNull(deletedAgreement);
    }

    @Test
    public void getTransfersByAgreementId_validId_shouldReturnTransfers() {
        List<TransferDTO> transferDTOList = agreementService.getTransfersByAgreementId("A1234", null);
        assertNotNull(transferDTOList);
        assertEquals(1, transferDTOList.size());
    }
    
}