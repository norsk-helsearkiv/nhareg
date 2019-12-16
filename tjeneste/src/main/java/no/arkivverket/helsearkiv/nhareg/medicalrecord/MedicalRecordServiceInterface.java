package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;

import javax.ws.rs.core.MultivaluedMap;

public interface MedicalRecordServiceInterface {

    Pasientjournal create(final Pasientjournal medicalRecord);
    
    Pasientjournal delete(final String id);
    
    Pasientjournal getById(final String id);
    
    MedicalRecordDTO getByIdWithTransfer(final String id);

    ListObject getAllWithTransfers(final MultivaluedMap<String, String> queryParameters);

    MedicalRecordDTO updateMedicalRecord(final MedicalRecordDTO medicalRecordDTO);
    
    void validatePID(final String pid);

    MedicalRecordDTO createInTransfer(final String transferId, final PersondataDTO personalDataDTO);
    
}