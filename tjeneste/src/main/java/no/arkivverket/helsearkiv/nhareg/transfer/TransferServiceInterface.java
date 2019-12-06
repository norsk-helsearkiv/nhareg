package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;

import javax.ws.rs.core.MultivaluedMap;

public interface TransferServiceInterface {

    Avlevering getAvlevering(final String id);

    ListObject getMedicalRecords(final String id, final MultivaluedMap<String, String> queryParameters);

}
