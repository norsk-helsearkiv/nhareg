package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface DiagnosisCodeServiceInterface {

    List<DiagnosisCode> getAll(final MultivaluedMap<String, String> queryParameters);

    DiagnosisCode create(final DiagnosisCode diagnosisCode);
    
}