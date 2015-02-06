package no.arkivverket.helsearkiv.nhareg.tjeneste;


import javax.inject.Inject;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PasientjournalTjenesteTest {
    
    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }
   
    @Inject
    private PasientjournalTjeneste tjeneste;
    
    @Test
    public void testCreate(){
        Pasientjournal a = new Pasientjournal();
        tjeneste.create(a);
    }
}
