package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.RecordTransferDTO;

import java.util.Collection;
import java.util.List;

public interface MedicalRecordConverterInterface {
    
    MedicalRecord fromMedicalRecordDTO(final MedicalRecordDTO medicalRecordDTO);

    MedicalRecordDTO toMedicalRecordDTO(final MedicalRecord medicalRecord);
    
    List<RecordTransferDTO> toRecordTransferDTOList(final Collection<MedicalRecord> medicalRecordList);

}