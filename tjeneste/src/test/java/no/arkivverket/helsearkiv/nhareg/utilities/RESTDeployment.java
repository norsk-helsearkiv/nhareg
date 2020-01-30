package no.arkivverket.helsearkiv.nhareg.utilities;

import no.arkivverket.helsearkiv.nhareg.agreement.*;
import no.arkivverket.helsearkiv.nhareg.archiveauthor.*;
import no.arkivverket.helsearkiv.nhareg.business.BusinessDAO;
import no.arkivverket.helsearkiv.nhareg.business.BusinessService;
import no.arkivverket.helsearkiv.nhareg.business.BusinessServiceInterface;
import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.common.ParameterConverter;
import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisConverter;
import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisConverterInterface;
import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisService;
import no.arkivverket.helsearkiv.nhareg.diagnosiscode.DiagnosisCodeDAO;
import no.arkivverket.helsearkiv.nhareg.diagnosiscode.DiagnosisCodeResource;
import no.arkivverket.helsearkiv.nhareg.diagnosiscode.DiagnosisCodeService;
import no.arkivverket.helsearkiv.nhareg.diagnosiscode.DiagnosisCodeServiceInterface;
import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.AdditionalInfo;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.UserDTO;
import no.arkivverket.helsearkiv.nhareg.domene.common.ValidDateFormats;
import no.arkivverket.helsearkiv.nhareg.domene.configuration.ConfigurationParameter;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateTimeConverter;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.gender.GenderDAO;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.*;
import no.arkivverket.helsearkiv.nhareg.storageunit.*;
import no.arkivverket.helsearkiv.nhareg.transfer.*;
import no.arkivverket.helsearkiv.nhareg.user.*;
import no.arkivverket.helsearkiv.nhareg.validation.DateValidation;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class RESTDeployment {

    public static WebArchive deployment() {
        return NharegDeployment.deployment()
                               // Models
                               .addPackage(AdditionalInfo.class.getPackage())
                               .addPackage(ConfigurationParameter.class.getPackage())
                               // DTOs
                               .addPackage(AgreementDTO.class.getPackage())
                               .addPackage(MedicalRecordDTO.class.getPackage())
                               .addPackage(UserDTO.class.getPackage())
                               // Entity
                               .addPackage(EntityDAO.class.getPackage())
                               // Agreements
                               .addPackage(AgreementDAO.class.getPackage())
                               .addPackage(AgreementService.class.getPackage())
                               .addPackage(AgreementServiceInterface.class.getPackage())
                               .addPackage(AgreementConverterInterface.class.getPackage())
                               .addPackage(AgreementConverter.class.getPackage())
                               // ArchiveAuthor
                               .addPackage(ArchiveAuthorDAO.class.getPackage())
                               .addPackage(ArchiveAuthorService.class.getPackage())
                               .addPackage(ArchiveAuthorServiceInterface.class.getPackage())
                               .addPackage(ArchiveAuthorConverterInterface.class.getPackage())
                               .addPackage(ArchiveAuthorConverter.class.getPackage())
                               // Business
                               .addPackage(BusinessDAO.class.getPackage())
                               .addPackage(BusinessService.class.getPackage())
                               .addPackage(BusinessServiceInterface.class.getPackage())
                               // Configuration
                               .addPackage(ConfigurationDAO.class.getPackage())
                               // Diagnosis
                               .addPackage(DiagnosisCodeDAO.class.getPackage())
                               .addPackage(DiagnosisService.class.getPackage())
                               .addPackage(DiagnosisCodeServiceInterface.class.getPackage())
                               .addPackage(DiagnosisConverterInterface.class.getPackage())
                               .addPackage(DiagnosisConverter.class.getPackage())
                               // DiagnosisCode
                               .addPackage(DiagnosisCodeResource.class.getPackage())
                               .addPackage(DiagnosisCodeService.class.getPackage())
                               .addPackage(DiagnosisCodeServiceInterface.class.getPackage())
                               .addPackage(DiagnosisCodeDAO.class.getPackage())
                               // Gender
                               .addPackage(GenderDAO.class.getPackage())
                               // Medical Record classes
                               .addPackage(MedicalRecordServiceInterface.class.getPackage())
                               .addPackage(MedicalRecordService.class.getPackage())
                               .addPackage(MedicalRecordConverter.class.getPackage())
                               .addPackage(MedicalRecordConverterInterface.class.getPackage())
                               .addPackage(MedicalRecordDAO.class.getPackage())
                               // Storage Units
                               .addPackage(StorageUnit.class.getPackage())
                               .addPackage(StorageUnitService.class.getPackage())
                               .addPackage(StorageUnitServiceInterface.class.getPackage())
                               .addPackage(StorageUnitDAO.class.getPackage())
                               .addPackage(StorageUnitConverterInterface.class.getPackage())
                               .addPackage(StorageUnitConverter.class.getPackage())
                               // Transfer classes
                               .addPackage(TransferService.class.getPackage())
                               .addPackage(TransferServiceInterface.class.getPackage())
                               .addPackage(TransferConverterInterface.class.getPackage())
                               .addPackage(TransferConverter.class.getPackage())
                               .addPackage(TransferDAO.class.getPackage())
                               // User
                               .addPackage(User.class.getPackage())
                               .addPackage(UserServiceInterface.class.getPackage())
                               .addPackage(UserService.class.getPackage())
                               .addPackage(UserConverterInterface.class.getPackage())
                               .addPackage(UserConverter.class.getPackage())
                               .addPackage(UserDAO.class.getPackage())
                               // Util
                               .addPackage(DateValidation.class.getPackage())
                               .addPackage(ListObject.class.getPackage())
                               .addPackage(LocalDateTimeConverter.class.getPackage())
                               .addPackage(ParameterConverter.class.getPackage())
                               .addPackage(ValidDateFormats.class.getPackage())
                               .addPackage(ValidationError.class.getPackage())
                               .addPackage(ValidationErrorException.class.getPackage())
            ;
    }
    
}