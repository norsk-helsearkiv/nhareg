package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.AvleveringDTO;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface AgreementServiceInterface {
    
    List<Avtale> getAll(final MultivaluedMap<String, String> queryParameters);

    List<AvleveringDTO> getTransfersById(final String id, final Avlevering defaultTransfer);
    
}