package no.arkivverket.helsearkiv.nhareg.tjeneste;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class NharegDeployment {

    public static WebArchive deployment() {
        return ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackage(Resources.class.getPackage())
//                .addAsResource("META-INF/test-persistence.xml")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/validation.xml" )
                .addAsResource( "constraints-car.xml" )
//                .addAsResource("import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml");
    }
}
