package no.arkivverket.helsearkiv.nhareg.transformer;


import javax.ejb.EJB;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.util.RESTDeployment;
import org.apache.commons.collections4.Transformer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DiagnoseFraDTOTransformerTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @EJB(name = "DiagnoseDTOTransformer")
    Transformer<DiagnoseDTO,Diagnose> diagnoseDTOTransformer;

    @Test
    public void transform() {
        DiagnoseDTO dto = new DiagnoseDTO();
        dto.setDiagnosetekst("tt");
        dto.setDiagnosekode("Code0");
        dto.setDiagnosedato("1998");
        Diagnose diagnose = diagnoseDTOTransformer.transform(dto);
        assertNotNull(diagnose);
        assertEquals(dto.getDiagnosetekst(), diagnose.getDiagnosetekst());
        assertNotNull(diagnose.getDiagnosekode());
        assertEquals(dto.getDiagnosekode(), diagnose.getDiagnosekode().getCode());
        assertNotNull(diagnose.getDiagdato());
        assertEquals(1998, diagnose.getDiagdato().getAar().intValue());
    }

}
