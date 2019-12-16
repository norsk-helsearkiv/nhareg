package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.utilities.AdminHandler;
import no.arkivverket.helsearkiv.nhareg.utilities.RESTDeployment;
import no.arkivverket.helsearkiv.nhareg.utilities.UserHandler;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class PasientjournalTjenesteTest {

    @Inject
    private PasientjournalTjeneste tjeneste;

    @Inject
    private UserHandler userHandler;
    
    @Inject
    private AdminHandler adminHandler;

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

//    @Test
//    public void getAll_tomInfo_skalGiTo() throws Exception {
//        userHandler.call(new Callable<Object>() {
//            @Override
//            public Object call() {
//                ListObject listeObjekt = tjeneste.hentAlle(new MockUriInfo());
//                assertEquals(2, listeObjekt.getTotal());
//
//                return null;
//            }
//        });
//    }

//    @Test
//    public void getActiveWithPaging_henterEnFraSideTo_andreElementIListen() throws Exception {
//        userHandler.call(new Callable<Object>() {
//            @Override
//            public Object call() {
//                //Henter alle pasientjournaler i databasen for test av paging
//                MultivaluedHashMap<String, String> map = new MultivaluedHashMap<String, String>();
//                List<Pasientjournal> pasientjournaler = tjeneste.fetchAll(map);
//
//                ListObject listObject = tjeneste.getActiveWithPaging(pasientjournaler, new MockUriInfoQPFilled());
//                assertEquals(2, listObject.getTotal());
//                assertEquals(1, listObject.getNumber());
//                List<PasientjournalSokeresultatDTO> resultatListe
//                    = (List<PasientjournalSokeresultatDTO>) listObject.getListe();
//                assertEquals("uuid1", resultatListe.get(0).getUuid());
//
//                return null;
//            }
//        });
//    }

    @Test
    public void getSingleInstance_henterForsteObjekt_returnererDTO() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                PasientjournalDTO pasientjournalDTO = tjeneste.getPasientjournalDTO("uuid1");

                assertEquals("Hunden Fido", pasientjournalDTO.getPersondata().getNavn());
                assertEquals(3, pasientjournalDTO.getPersondata().getLagringsenheter().length);
                assertFalse(pasientjournalDTO.getDiagnoser().isEmpty());

                return null;
            }
        });
    }

    @Test
    public void leggTilDiagnose_nyDiagnose_skalOkeDiagnoseMedEn() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Pasientjournal pasientjournal = tjeneste.hent("uuid1");
                assertNotNull(pasientjournal);
                assertNotNull(pasientjournal.getDiagnose());

                // Henter antall diagnoser før.
                int antallDiagnoser = pasientjournal.getDiagnose().size();

                // Henter antall lagringsenheter før.
                assertNotNull(pasientjournal.getLagringsenhet());
                int antallLagringsenheter = pasientjournal.getLagringsenhet().size();

                // Legger til diagnose.
                DiagnoseDTO dto = new DiagnoseDTO();
                dto.setDiagnosedato("15.01.2005");
                dto.setDiagnosetekst("Jeg er syk");
                dto.setDiagnosekode("Code0");
                try {
                    Response response = tjeneste.leggTilDiagnose("uuid1", dto);
                    assertEquals(200, response.getStatus());
                } catch (ValideringsfeilException vfe) {
                    Valideringsfeil valideringsfeil = vfe.getValideringsfeil().get(0);
                    assert false;
                }


                // Sjekker at antall diagnoser har økt med 1.
                pasientjournal = tjeneste.hent("uuid1");
                assertNotNull(pasientjournal);
                assertNotNull(pasientjournal.getDiagnose());
                assertEquals(antallDiagnoser + 1, pasientjournal.getDiagnose().size());

                // Sjekker at antall lagringsenheter er det samme
                assertNotNull(pasientjournal.getLagringsenhet());
                assertEquals(antallLagringsenheter, pasientjournal.getLagringsenhet().size());

                return null;
            }
        });
    }

    @Test
    public void fjernDiagnose_fjernEksisterendeDiagnose_skalMinskeDiagnoserMedEn() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Pasientjournal pasientjournal = tjeneste.hent("uuid1");

                assertNotNull(pasientjournal);
                assertNotNull(pasientjournal.getDiagnose());
                assertFalse(pasientjournal.getDiagnose().isEmpty());

                int antallDiagnoser = pasientjournal.getDiagnose().size();
                DiagnoseDTO diagnoseDTO = new DiagnoseDTO();
                diagnoseDTO.setUuid(pasientjournal.getDiagnose().iterator().next().getUuid());
                Response response = tjeneste.fjernDiagnose("uuid1", diagnoseDTO);
                assertNotNull(response);
                assertEquals(200, response.getStatus());

                pasientjournal = tjeneste.hent("uuid1");
                assertNotNull(pasientjournal);
                assertNotNull(pasientjournal.getDiagnose());
                assertEquals(antallDiagnoser - 1, pasientjournal.getDiagnose().size());

                return null;
            }
        });
    }

    @Test(expected = ValideringsfeilException.class)
    public void leggTilDiagnose_nullDto_skalGiValideringsFeil() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() throws ValideringsfeilException {
                tjeneste.leggTilDiagnose("uuid1", null);
            
                return null;
            }
        });
    }

    @Test
    public void oppdaterPasientjournal_setterNyttJournalnummer_skalIkkeKasteException() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() throws ParseException {
                PasientjournalDTO pasientjournalDTO = tjeneste.getPasientjournalDTO("uuid1");
                pasientjournalDTO.getPersondata().setJournalnummer("12345");
                tjeneste.oppdaterPasientjournal(pasientjournalDTO);

                return null;
            }
        });
    }

    @Test
    public void oppdaterPasientjournal_nyBeskrivelse_skalBliOppdatert() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() throws ParseException {
                PasientjournalDTO pasientjournalDTO = tjeneste.getPasientjournalDTO("uuid1");
                assertNotNull(pasientjournalDTO);

                final String beskrivelse = "ny beskrivelse";
                pasientjournalDTO.setAvleveringBeskrivelse(beskrivelse);
                Response response = tjeneste.oppdaterPasientjournal(pasientjournalDTO);

                assertEquals(200, response.getStatus());

                pasientjournalDTO = tjeneste.getPasientjournalDTO("uuid1");
                assertNotNull(pasientjournalDTO);
                assertEquals(beskrivelse, pasientjournalDTO.getAvleveringBeskrivelse());

                return null;
            }
        });
    }

    @Test
    public void oppdaterPasientjournal_skalIkkeEndreAntallLagringsenheter() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() throws ParseException {
                final String id = "uuid1";
                Pasientjournal pasientjournal = tjeneste.hent(id);
                assertNotNull(pasientjournal);
                assertNotNull(pasientjournal.getLagringsenhet());
                assertEquals(3, pasientjournal.getLagringsenhet().size());

                PasientjournalDTO pasientjournalDTO = tjeneste.getPasientjournalDTO(id);
                assertNotNull(pasientjournalDTO);
                assertNotNull(pasientjournalDTO.getPersondata());
                assertNotNull(pasientjournalDTO.getPersondata().getLagringsenheter());
                assertEquals(3, pasientjournalDTO.getPersondata().getLagringsenheter().length);

                // Gjør en oppdatering.
                tjeneste.oppdaterPasientjournal(pasientjournalDTO);

                // Sjekker antall diagnoser som er lagret
                pasientjournalDTO = tjeneste.getPasientjournalDTO("uuid1");
                assertNotNull(pasientjournalDTO);
                assertNotNull(pasientjournalDTO.getPersondata());
                assertNotNull(pasientjournalDTO.getPersondata().getLagringsenheter());
                assertEquals(3, pasientjournalDTO.getPersondata().getLagringsenheter().length);
                
                return null;
            }
        });
    }

    @Test
    public void leggTilDiagnose_nyDiagnoseOgOppdatertFodtDato_skalHaNyFodtDato() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() throws ParseException {
                // Oppretter diagnoser uten diagnosekoder, men med tekst og lagrer.
                // Tar opp igjen pasientjournalen og endrer f.eks. dato.
                // Resultatet er at de registrerte diagnosetekstene er forsvunnet.
                final String id = "uuid1";
                PasientjournalDTO pasientjournalDTO = tjeneste.getPasientjournalDTO(id);
                assertNotNull(pasientjournalDTO);
        
                // Legger til diagnose.
                DiagnoseDTO diagnoseDTO = new DiagnoseDTO();
                diagnoseDTO.setDiagnosedato("15.01.2005");
                diagnoseDTO.setDiagnosetekst("Jeg er syk");
                Response response = tjeneste.leggTilDiagnose(id, diagnoseDTO);
                assertNotNull(response);
                assertEquals(200, response.getStatus());
        
                final String fodt = "1990";
                pasientjournalDTO = tjeneste.getPasientjournalDTO(id);
                assertNotNull(pasientjournalDTO);
                pasientjournalDTO.getPersondata().setFodt(fodt);
                // Oppdater
                response = tjeneste.oppdaterPasientjournal(pasientjournalDTO);
                assertEquals(200, response.getStatus());
                
                pasientjournalDTO = tjeneste.getPasientjournalDTO(id);
                assertEquals(fodt, pasientjournalDTO.getPersondata().getFodt());
                
                return null;
            }
        });
    }

    @Test
    public void delete_ugyldigId_skalKasteNoResultException() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                try {
                    tjeneste.delete("tull");
                } catch (EJBException e) {
                    assertEquals(NoResultException.class, e.getCause().getClass());
                }
                
                return null;
            }
        });
    }

    @Test
    public void delete_gyldigId_skalSetteSlettetTilTrue() throws Exception {
        userHandler.call(new Callable<Object>() {
            @Override
            public Object call() {
                Pasientjournal pasientjournal = tjeneste.delete("uuid1");
                assertNotNull(pasientjournal);
                assertEquals(true, pasientjournal.getSlettet());
                
                return null;
            }
        });
    }

}
