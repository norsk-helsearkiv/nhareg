package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.PersonalDataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.RecordTransferDTO;

import java.util.Collection;
import java.util.List;

public interface MedicalRecordConverterInterface {
    
    MedicalRecord fromPersonalDataDTO(final PersonalDataDTO personalDataDTO);

    PersonalDataDTO toPersonalDataDTO(final MedicalRecord medicalRecord);
    
    MedicalRecordDTO toMedicalRecordDTO(final MedicalRecord medicalRecord, final Transfer transfer, 
                                        final String business);

    RecordTransferDTO toRecordTransferDTO(final MedicalRecord medicalRecord);

    List<RecordTransferDTO> toRecordTransferDTOList(final Collection<MedicalRecord> medicalRecordList);

}