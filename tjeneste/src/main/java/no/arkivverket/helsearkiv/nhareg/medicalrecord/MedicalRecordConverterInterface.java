package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.PersonalDataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.RecordTransferDTO;

import java.util.Collection;
import java.util.List;

public interface MedicalRecordConverterInterface {
    
    MedicalRecord fromPersonalDataDTO(PersonalDataDTO personalDataDTO);

    PersonalDataDTO toPersonalDataDTO(MedicalRecord medicalRecord);
    
    MedicalRecordDTO toMedicalRecordDTO(MedicalRecord medicalRecord, Transfer transfer, String business);

    RecordTransferDTO toRecordTransferDTO(MedicalRecord medicalRecord);

    List<RecordTransferDTO> toRecordTransferDTOList(Collection<MedicalRecord> medicalRecordList);
    
}