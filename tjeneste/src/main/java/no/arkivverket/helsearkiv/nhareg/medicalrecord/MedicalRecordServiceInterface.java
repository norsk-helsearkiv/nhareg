package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ListObject;

import javax.ws.rs.core.MultivaluedMap;

public interface MedicalRecordServiceInterface {

    MedicalRecordDTO create(final MedicalRecordDTO medicalRecordDTO, final String username);

    MedicalRecordDTO update(final MedicalRecordDTO medicalRecordDTO, final String username);
    
    MedicalRecordDTO delete(final String id, final String username);
    
    MedicalRecordDTO getById(final String id);
    
    MedicalRecordDTO getByIdWithTransfer(final String id);

    ListObject getAllWithTransfers(final MultivaluedMap<String, String> queryParameters, final String id);
    
    void validatePID(final String pid);
    
}