package no.arkivverket.helsearkiv.nhareg.transformer;


import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DiagnoseFraDTOTransformerTest {

    @Test
    public void dummy() {
        assert(true);
    }
//    @Deployment
//    public static WebArchive deployment() {
//        return RESTDeployment.deployment();
//    }
//
//    @EJB(name = "DiagnoseDTOTransformer")
//    Transformer<DiagnoseDTO,Diagnose> diagnoseDTOTransformer;
//
//    @Test
//    public void transform() {
//        DiagnoseDTO dto = new DiagnoseDTO();
//        dto.setDiagnosetekst("tt");
//        dto.setDiagnosekode("Code0");
//        dto.setDiagnosedato("1998");
//        Diagnose diagnose = diagnoseDTOTransformer.transform(dto);
//        assertNotNull(diagnose);
//        assertEquals(dto.getDiagnosetekst(), diagnose.getDiagnosetekst());
//        assertNotNull(diagnose.getDiagnosekode());
//        assertEquals(dto.getDiagnosekode(), diagnose.getDiagnosekode().getCode());
//        assertNotNull(diagnose.getDiagdato());
//        assertEquals(1998, diagnose.getDiagdato().getAar().intValue());
//    }

}
