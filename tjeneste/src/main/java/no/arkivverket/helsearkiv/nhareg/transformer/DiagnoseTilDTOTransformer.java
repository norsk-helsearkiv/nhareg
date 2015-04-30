package no.arkivverket.helsearkiv.nhareg.transformer;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import org.apache.commons.collections4.Transformer;

/**
 *
 * @author arnfinns
 */
public class DiagnoseTilDTOTransformer implements Transformer<Diagnose, DiagnoseDTO> {

    DatoEllerAarTilStringTransformer datoEllerAarTilStringTransformer = new DatoEllerAarTilStringTransformer();

    public DiagnoseDTO transform(Diagnose input) {
        DiagnoseDTO dto = null;
        if (input != null) {
            dto = new DiagnoseDTO();
            dto.setUuid(input.getUuid());
            dto.setDiagnosetekst(input.getDiagnosetekst());
            dto.setDiagnosedato(datoEllerAarTilStringTransformer.transform(input.getDiagdato()));
            if (input.getDiagnosekode() != null) {
                dto.setDiagnosekode(input.getDiagnosekode().getCode());
                dto.setDiagnosekodeverk(input.getDiagnosekode().getCodeSystemVersion());
            }
            if (input.getOppdateringsinfo()!=null) {
                dto.setOppdatertAv(input.getOppdateringsinfo().getOppdatertAv());
            }

        }
        return dto;
    }

}
