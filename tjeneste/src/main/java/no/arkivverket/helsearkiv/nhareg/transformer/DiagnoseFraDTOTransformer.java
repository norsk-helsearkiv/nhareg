package no.arkivverket.helsearkiv.nhareg.transformer;

//import org.apache.commons.collections.Transformer;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
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
            MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
            queryParameters.add("code", input.getDiagnosekode());
            List<Diagnosekode> list = tjeneste.getAll(queryParameters);
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
