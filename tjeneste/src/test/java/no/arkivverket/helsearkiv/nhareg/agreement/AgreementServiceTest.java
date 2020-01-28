package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Business;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.BusinessDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Inject
    private AgreementConverterInterface agreementConverter;

    @Test(expected = ValidationErrorException.class)
    public void create_missingBusiness_shouldThrowValidationError() {
        final String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMuuuu"));
        final AgreementDTO agreement = new AgreementDTO("enhet-4", now, "boks4", null);
        final AgreementDTO newAgreement = agreementService.create(agreement);

        assertNotNull(newAgreement);
        assertEquals(newAgreement, agreement);
    }

    @Test
    public void create_validAgreement_shouldReturnAgreement() {
        final String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMuuuu"));
        final BusinessDTO businessDTO = new BusinessDTO("100", "Testorganisasjon", null);
        final AgreementDTO agreement = new AgreementDTO("enhet-4", now, "boks4", businessDTO);
        final AgreementDTO newAgreement = agreementService.create(agreement);
        
        assertNotNull(newAgreement);
        assertEquals(agreement, newAgreement);
    }

    @Test
    public void getAll_emptyQuery_shouldReturnTwo() {
        final List<AgreementDTO> agreementList = agreementService.getAll(new MultivaluedHashMap<>());
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
        final Agreement agreement = new Agreement();
        final Business business = new Business();
        final String agreementId = "test-avtale";

        agreement.setAgreementDescription("beskrivelse");
        agreement.setAgreementId(agreementId);
        agreement.setAgreementDate(LocalDateTime.now());

        business.setOrganizationNumber("100");
        business.setName("Testorganisasjon");
        agreement.setBusiness(business);

        final AgreementDTO agreementDTO = agreementConverter.fromAgreement(agreement);
        final AgreementDTO newAgreement = agreementService.create(agreementDTO);
        assertNotNull(newAgreement);

        final AgreementDTO deletedAgreement = agreementService.delete(agreementId);
        assertNotNull(deletedAgreement);
    }

    @Test
    public void getTransfersByAgreementId_validId_shouldNotReturnDuplicates() {
        final List<TransferDTO> transferDTOList = agreementService.getTransfersByAgreementId("Avtale1", null);
        assertNotNull(transferDTOList);
        assertEquals(1, transferDTOList.size());
    }

    @Test
    public void getTransfersByAgreementId_validId_shouldReturnTransfers() {
        final List<TransferDTO> transferDTOList = agreementService.getTransfersByAgreementId("A1234", null);
        assertNotNull(transferDTOList);
        assertEquals(1, transferDTOList.size());
    }

}