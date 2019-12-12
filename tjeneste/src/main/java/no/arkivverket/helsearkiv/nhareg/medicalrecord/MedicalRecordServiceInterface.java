package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;

import javax.ws.rs.core.MultivaluedMap;

public interface MedicalRecordServiceInterface {

    MedicalRecordDTO getByIdWithTransfer(String id);

    ListObject getAllWithTransfers(MultivaluedMap<String, String> queryParameters);
    
    String getTransferIdFromMedicalRecord(String id);

}
