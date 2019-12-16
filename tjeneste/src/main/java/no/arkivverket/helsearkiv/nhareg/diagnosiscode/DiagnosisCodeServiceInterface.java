package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface DiagnosisCodeServiceInterface {

    List<Diagnosekode> getAll(final MultivaluedMap<String, String> queryParameters);

    Diagnosekode create(final Diagnosekode diagnosisCode);
    
}