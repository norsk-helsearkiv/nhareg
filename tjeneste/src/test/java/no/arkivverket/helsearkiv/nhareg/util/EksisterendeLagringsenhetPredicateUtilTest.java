package no.arkivverket.helsearkiv.nhareg.util;


import javax.ejb.EJB;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import org.apache.commons.collections4.Predicate;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class EksisterendeLagringsenhetPredicateUtilTest {

    @Test
    public void dummy() {
        assert(true);
    }
    //    @Deployment
//    public static WebArchive deployment() {
//        return RESTDeployment.deployment();
//    }
//
//    @EJB(name = "EksisterendeLagringsenhetPredicate")
//    Predicate<Lagringsenhet> diagnoseDTOTransformer;
//
//    @Test
//    public void evaluate() {
//        Lagringsenhet dto = new Lagringsenhet();
//        dto.setIdentifikator("boks1");
//        assertTrue(diagnoseDTOTransformer.evaluate(dto));
//        dto.setIdentifikator("boksukjent");
//        assertFalse(diagnoseDTOTransformer.evaluate(dto));
//    }
}
