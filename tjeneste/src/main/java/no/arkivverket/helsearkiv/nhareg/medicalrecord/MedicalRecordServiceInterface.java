package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import javax.ws.rs.core.MultivaluedMap;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;

public interface MedicalRecordServiceInterface {

    Pasientjournal getById(String id);

    ListObject getAll(MultivaluedMap<String, String> queryParameters);

    String getDeliveryIdFromMedicalRecord(String id);
}
