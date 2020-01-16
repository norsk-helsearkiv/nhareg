package no.arkivverket.helsearkiv.nhareg.utilities;

import no.arkivverket.helsearkiv.nhareg.agreement.AgreementDAO;
import no.arkivverket.helsearkiv.nhareg.agreement.AgreementResource;
import no.arkivverket.helsearkiv.nhareg.agreement.AgreementService;
import no.arkivverket.helsearkiv.nhareg.agreement.AgreementServiceInterface;
import no.arkivverket.helsearkiv.nhareg.archivecreator.ArchiveCreatorDAO;
import no.arkivverket.helsearkiv.nhareg.archivecreator.ArchiveCreatorService;
import no.arkivverket.helsearkiv.nhareg.archivecreator.ArchiveCreatorServiceInterface;
import no.arkivverket.helsearkiv.nhareg.business.BusinessDAO;
import no.arkivverket.helsearkiv.nhareg.business.BusinessService;
import no.arkivverket.helsearkiv.nhareg.business.BusinessServiceInterface;
import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisResource;
import no.arkivverket.helsearkiv.nhareg.diagnosis.DiagnosisService;
import no.arkivverket.helsearkiv.nhareg.diagnosiscode.DiagnosisCodeDAO;
import no.arkivverket.helsearkiv.nhareg.diagnosiscode.DiagnosisCodeServiceInterface;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.BrukerDTO;
import no.arkivverket.helsearkiv.nhareg.domene.common.ValidDateFormats;
import no.arkivverket.helsearkiv.nhareg.domene.configuration.ConfigurationParameter;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.converter.LocalDateTimeConverter;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveCreator;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DateOrYear;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.PersonalDataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.gender.GenderDAO;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.*;
import no.arkivverket.helsearkiv.nhareg.storageunit.StorageUnitDAO;
import no.arkivverket.helsearkiv.nhareg.storageunit.StorageUnitResource;
import no.arkivverket.helsearkiv.nhareg.storageunit.StorageUnitService;
import no.arkivverket.helsearkiv.nhareg.storageunit.StorageUnitServiceInterface;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferDAO;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferResource;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferService;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferServiceInterface;
import no.arkivverket.helsearkiv.nhareg.user.UserDAO;
import no.arkivverket.helsearkiv.nhareg.util.ParameterConverter;
import no.arkivverket.helsearkiv.nhareg.validation.DateValidation;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class RESTDeployment {

    public static WebArchive deployment() {
        return NharegDeployment.deployment()
                               // Models
                               .addPackage(ArchiveCreator.class.getPackage())
                               .addPackage(Agreement.class.getPackage())
                               .addPackage(User.class.getPackage())
                               .addPackage(DateOrYear.class.getPackage())
                               .addPackage(ValidDateFormats.class.getPackage())
                               .addPackage(ConfigurationParameter.class.getPackage())
                               .addPackage(ListObject.class.getPackage())
                               .addPackage(ValidationErrorException.class.getPackage())
                               // DTOs
                               .addPackage(BrukerDTO.class.getPackage())
                               .addPackage(MedicalRecordDTO.class.getPackage())
                               .addPackage(PersonalDataDTO.class.getPackage())
                               // Entity
                               .addPackage(EntityDAO.class.getPackage())
                               // Agreements
                               .addPackage(AgreementDAO.class.getPackage())
                               .addPackage(AgreementResource.class.getPackage())
                               .addPackage(AgreementService.class.getPackage())
                               .addPackage(AgreementServiceInterface.class.getPackage())
                               // ArchiveCreator
                               .addPackage(ArchiveCreatorDAO.class.getPackage())
                               .addPackage(ArchiveCreatorService.class.getPackage())
                               .addPackage(ArchiveCreatorServiceInterface.class.getPackage())
                               // Business
                               .addPackage(BusinessDAO.class.getPackage())
                               .addPackage(BusinessService.class.getPackage())
                               .addPackage(BusinessServiceInterface.class.getPackage())
                               // Configuration
                               .addPackage(ConfigurationDAO.class.getPackage())
                               // Diagnosis
                               .addPackage(DiagnosisResource.class.getPackage())
                               .addPackage(DiagnosisCodeDAO.class.getPackage())
                               .addPackage(DiagnosisService.class.getPackage())
                               .addPackage(DiagnosisCodeServiceInterface.class.getPackage())
                               // Gender
                               .addPackage(GenderDAO.class.getPackage())
                               // Medical Record classes
                               .addPackage(MedicalRecordServiceInterface.class.getPackage())
                               .addPackage(MedicalRecordService.class.getPackage())
                               .addPackage(MedicalRecordConverter.class.getPackage())
                               .addPackage(MedicalRecordResource.class.getPackage())
                               .addPackage(MedicalRecordDAO.class.getPackage())
                               // Transfer classes
                               .addPackage(TransferService.class.getPackage())
                               .addPackage(TransferServiceInterface.class.getPackage())
                               .addPackage(TransferResource.class.getPackage())
                               .addPackage(TransferDAO.class.getPackage())
                               // Storage Units
                               .addPackage(StorageUnitResource.class.getPackage())
                               .addPackage(StorageUnitService.class.getPackage())
                               .addPackage(StorageUnitServiceInterface.class.getPackage())
                               .addPackage(StorageUnitDAO.class.getPackage())
                               // User
                               .addPackage(UserDAO.class.getPackage())
                               //util
                               .addPackage(DateValidation.class.getPackage())
                               .addPackage(ParameterConverter.class.getPackage())
                               .addPackage(LocalDateTimeConverter.class.getPackage());
    }
}