package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;

import javax.ws.rs.core.MultivaluedMap;

public interface MedicalRecordServiceInterface {

    Pasientjournal getById(String id);

    ListObject getAll(MultivaluedMap<String, String> queryParameters);

    ListObject getAllWithTransfers(MultivaluedMap<String, String> queryParameters);
    
    String getTransferIdFromMedicalRecord(String id);

}
