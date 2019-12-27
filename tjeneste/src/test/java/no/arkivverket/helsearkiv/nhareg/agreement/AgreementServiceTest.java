package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
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
    public void getAll_emptyQuery_shouldReturnTwo() {
        final List<Avtale> agreementList = agreementService.getAll(new MultivaluedHashMap<>());
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

    @Test(expected = ValideringsfeilException.class)
    public void delete_validIdWithChildren_shouldThrowValideringsfeilException() {
        agreementService.delete("Avtale1");
    }

    @Test
    public void delete_validIdWithoutChildren_shouldReturnAgreement() {
        final Avtale transfer = new Avtale();
        final Calendar calendar = Calendar.getInstance();
        final Virksomhet virksomhet = new Virksomhet();
        final String agreementId = "test-avtale";

        transfer.setAvtalebeskrivelse("beskrivelse");
        transfer.setAvtaleidentifikator(agreementId);
        transfer.setAvtaledato(calendar);

        virksomhet.setOrganisasjonsnummer("100");
        virksomhet.setNavn("Testorganisasjon");
        transfer.setVirksomhet(virksomhet);
        
        final Avtale newAgreement = agreementService.create(transfer);
        assertNotNull(newAgreement);

        final Avtale deletedAgreement = agreementService.delete(agreementId);
        assertNotNull(deletedAgreement);
    }

}