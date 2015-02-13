package no.arkivverket.helsearkiv.nhareg.transformer;

//import org.apache.commons.collections.Transformer;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.tjeneste.DiagnosekodeTjeneste;
import org.apache.commons.collections4.Transformer;

/**
 *
 * @author arnfinns
 */
@Stateless
public class DiagnoseFraDTOTransformer implements Transformer<DiagnoseDTO, Diagnose> {

    @Inject
    private DiagnosekodeTjeneste tjeneste;
    private StringTilDatoEllerAarTransformer stringTilDatoEllerAarTransformer = new StringTilDatoEllerAarTransformer();

    public Diagnose transform(DiagnoseDTO input) {
        Diagnose diagnose = null;
        if (input != null) {
            diagnose = new Diagnose();
            diagnose.setUuid(input.getUuid());
            diagnose.setDiagnosetekst(input.getDiagnosetekst());
            List<Diagnosekode> list = tjeneste.hentDiagnosekoderMedCode(input.getDiagnosekode());
            if (list.size() == 1) {
                diagnose.setDiagnosekode(list.get(0));
            } else {
                throw new IllegalArgumentException(input.getDiagnosekode());
            }
            diagnose.setDiagdato(stringTilDatoEllerAarTransformer.transform(input.getDiagnosedato()));
        }
        return diagnose;
    }

}
