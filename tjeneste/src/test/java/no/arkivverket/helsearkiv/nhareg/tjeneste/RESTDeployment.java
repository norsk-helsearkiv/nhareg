package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class RESTDeployment {

    public static WebArchive deployment() {


        return NharegDeployment.deployment()
                .addPackage(Avtale.class.getPackage())
                .addPackage(EntitetsTjeneste.class.getPackage());
//                .addClass(AllocatedSeats.class)
//                .addClass(MediaPath.class)
//                .addClass(MediaManager.class);
    }
    
}
