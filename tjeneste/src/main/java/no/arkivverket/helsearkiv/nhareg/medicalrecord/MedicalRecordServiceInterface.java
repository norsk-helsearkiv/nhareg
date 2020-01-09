package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;

import javax.ws.rs.core.MultivaluedMap;

public interface MedicalRecordServiceInterface {

    MedicalRecord create(final MedicalRecord medicalRecord, final String username);
    
    MedicalRecord delete(final String id, final String username);
    
    MedicalRecord getById(final String id);
    
    MedicalRecordDTO getByIdWithTransfer(final String id);

    ListObject getAllWithTransfers(final MultivaluedMap<String, String> queryParameters);

    MedicalRecordDTO updateMedicalRecord(final MedicalRecordDTO medicalRecordDTO, final String username);
    
    void validatePID(final String pid);

    MedicalRecordDTO createInTransfer(final String transferId, final PersondataDTO personalDataDTO,
                                      final String username);
    
}