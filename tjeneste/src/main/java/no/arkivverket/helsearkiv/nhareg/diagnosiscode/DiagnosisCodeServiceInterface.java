package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisCodeDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface DiagnosisCodeServiceInterface {

    List<DiagnosisCodeDTO> getAll(final MultivaluedMap<String, String> queryParameters);

    DiagnosisCodeDTO create(final DiagnosisCodeDTO diagnosisCodeDTO);

    List<DiagnosisCodeDTO> getAllByCode(final String code, final MultivaluedMap<String, String> queryParameters);

    DiagnosisCodeDTO delete(final String id);
    
}