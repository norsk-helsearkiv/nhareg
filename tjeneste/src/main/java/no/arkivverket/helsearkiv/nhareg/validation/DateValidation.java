package no.arkivverket.helsearkiv.nhareg.validation;

import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.common.ValidDateFormats;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DateOrYear;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DateValidation {

    private final List<String> validMors = Arrays.asList("mors", "m", "M");
    private final List<String> validUnknown = Arrays.asList("ukjent", "u", "U");

    public List<ValidationError> validateDiagnosis(final DiagnosisDTO diagnosis, final MedicalRecord medicalRecord) {
        final List<ValidationError> validationErrors = new ArrayList<>();
        final Boolean bornDateUnknown = medicalRecord.getBornDateUnknown();
        final Boolean deathDateUnknown = medicalRecord.getDeathDateUnknown();
        
        if (bornDateUnknown != null && bornDateUnknown && deathDateUnknown != null && deathDateUnknown) {
            return validationErrors;
        }

        final String diagnosisDateString = diagnosis.getDiagnosisDate();
        final LocalDate diagnosisDate = getDate(diagnosisDateString);

        if (diagnosisDate == null) {
            validationErrors.add(new ValidationError("diagnosedato", "DiagFormatFeil"));
            return validationErrors;
        }

        // Check if diagnosis date is before being born
        if (bornDateUnknown == null || !bornDateUnknown) {
            final DateOrYear born = medicalRecord.getBorn();
            final LocalDateTime bornDate = born.getDate();

            if (bornDate != null && diagnosisDate.isBefore(bornDate.toLocalDate())) {
                validationErrors.add(new ValidationError("diagnosedato", "DiagForFodt"));
            } else if (bornDate == null) {
                validationErrors.add(new ValidationError("diagnosedato", "NotNull"));
            }
        }

        // Check if diagnosis date is after dying
        if (deathDateUnknown == null || !deathDateUnknown) {
            final DateOrYear dead = medicalRecord.getDead();
            final LocalDateTime deadDate = dead.getDate();
            
            if (deadDate != null && diagnosisDate.isAfter(deadDate.toLocalDate())) {
                validationErrors.add(new ValidationError("diagnosedato", "DiagEtterDod"));
            }
        }

        return validationErrors;
    }

    public ArrayList<ValidationError> validate(final PersonalDataDTO personalDataDTO, 
                                               final ConfigurationDAO configurationDAO) {
        final ArrayList<ValidationError> validationErrors = new ArrayList<>();

        if (medicalRecordDTO == null) {
            return validationErrors;
        }

        final String born = medicalRecordDTO.getBorn();
        final String dead = medicalRecordDTO.getDead();
        
        if (checkMorsUnknown(born, "mors") && checkMorsUnknown(born, "m")) {
            validationErrors.add(new ValidationError("fodt", "DagEllerAar"));
        }

        if (checkMorsUnknown(dead, "ukjent") && checkMorsUnknown(dead, "u")) {
            validationErrors.add(new ValidationError("dod", "DagEllerAar"));
        }

        if (validationErrors.size() > 0) {
            return validationErrors;
        }

        final LocalDate lowLim = configurationDAO.getDate(ConfigurationDAO.CONFIG_LOWLIM);
        final Integer waitLim = configurationDAO.getInt(ConfigurationDAO.CONFIG_WAITLIM);
        final LocalDate maxLim = LocalDate.now().minusYears(waitLim);
        final Integer maxAge = configurationDAO.getInt(ConfigurationDAO.CONFIG_MAXAGE);
        final LocalDate minLim = LocalDate.now().minusYears(maxAge);
        
        //skjema 1
        final List<ValidationError> pidErrors = pidCheck(medicalRecordDTO, lowLim, maxLim);
        validationErrors.addAll(pidErrors);

        //skjema 2a
        if (validUnknown.contains(born)) { //mors sjekkes i metoden pga feilmelding hvis den mangler
            final List<ValidationError> errors = recordDateUnknownFAndMors(medicalRecordDTO, lowLim, maxLim);
            validationErrors.addAll(errors);
        }

        //skjema 2b
        if (validMors.contains(dead) && check(born)) {
            final List<ValidationError> errors = recordDateKnownFDateUnknownDeath(medicalRecordDTO, minLim);
            validationErrors.addAll(errors);
        }

        //skjema 2c
        if (validUnknown.contains(born) && check(dead)) {
            final List<ValidationError> errors = recordDateKnownUnknownBornDate(medicalRecordDTO, lowLim, maxLim);
            validationErrors.addAll(errors);
        }

        //skjema 2d
        if (check(born) && check(dead)) {
            final List<ValidationError> errors = recordDateKnownBornAndMors(medicalRecordDTO, lowLim, maxLim);
            validationErrors.addAll(errors);
        }

        return validationErrors;
    }

    //skjema 01
    private ArrayList<ValidationError> pidCheck(final MedicalRecordDTO person,
                                                final LocalDate lowLim,
                                                final LocalDate maxLim) {
        final ArrayList<ValidationError> errors = new ArrayList<>();
        final String pid = person.getPid();
        final String personBorn = person.getBorn();

        if (!validUnknown.contains(personBorn)) {
            if (pid == null || pid.isEmpty()) {
                if (check(personBorn)) {
                    final LocalDate born = getDate(personBorn);

                    if (born.isBefore(lowLim) || born.isAfter(maxLim)) {
                        errors.add(new ValidationError("fodt", "UtenforGyldigPeriode",
                                                       "Person born outside valid period."));
                    }
                }
            } else {
                final ValidationError fnrError = PIDValidation.validate(person);
                if (fnrError != null) {
                    errors.add(fnrError);
                }
            }
        }

        return errors;
    }

    //skjema 02a
    private ArrayList<ValidationError> recordDateUnknownFAndMors(final PersonalDataDTO personalDataDTO,
                                                                 final LocalDate lowLim,
                                                                 final LocalDate maxLim) {
        final ArrayList<ValidationError> validationErrors = new ArrayList<>();
        final String lastContact = personalDataDTO.getLastContact();
        final String firstContact = personalDataDTO.getFirstContact();
        final String dead = personalDataDTO.getDead();

        if (validMors.contains(dead)) {
            checkFirstAfterLastContact(validationErrors, firstContact, lastContact);
            if (check(lastContact) && check(firstContact)) {
                final LocalDate firstContactDate = getDate(firstContact);
                final LocalDate lastContactDate = getDate(lastContact);
                
                if (lastContactDate.isBefore(firstContactDate)) {
                    validationErrors.add(new ValidationError("sKontakt", "sKontaktForfKontakt"));
                }

                if (lastContactDate.isBefore(lowLim) || lastContactDate.isAfter(maxLim)) {
                    validationErrors.add(new ValidationError("sKontakt", "UtenforGyldigPeriode"));
                }
            }
        } else if (!check(dead)) {
            validationErrors.add(new ValidationError("dod", "manglermors"));
        }

        return validationErrors;
    }

    //skjema 02b
    private List<ValidationError> recordDateKnownFDateUnknownDeath(final PersonalDataDTO personalDataDTO,
                                                                   final LocalDate maxAge) {
        final ArrayList<ValidationError> validationErrorList = new ArrayList<>();

        if (check(personalDataDTO.getDead()) || validMors.contains(personalDataDTO.getDead())) {
            final List<ValidationError> validationErrors = checkContactDates(personalDataDTO);
            if (!validationErrors.isEmpty()) {
                validationErrorList.addAll(validationErrors);
            }
        } else {
            if (check(personalDataDTO.getBorn())) {
                final LocalDate born = getDate(personalDataDTO.getBorn());

                if (born.isBefore(maxAge)) {
                    validationErrorList.add(new ValidationError("fodt", "UtenforGyldigPeriode"));
                } else {
                    final List<ValidationError> validationErrors = checkContactDates(personalDataDTO);
                    if (!validationErrors.isEmpty()) {
                        validationErrorList.addAll(validationErrors);
                    }
                }
            }
        }

        return validationErrorList;
    }

    //skjema 02c
    private List<ValidationError> recordDateKnownUnknownBornDate(final PersonalDataDTO personalDataDTO,
                                                                 final LocalDate lowLim,
                                                                 final LocalDate maxLim) {
        final ArrayList<ValidationError> validationErrors = new ArrayList<>();
        final LocalDate dead = getDate(personalDataDTO.getDead());

        if (dead.isBefore(lowLim) || dead.isAfter(maxLim)) {
            validationErrors.add(new ValidationError("dod", "UtenforGyldigPeriode"));
        } else {
            final List<ValidationError> errors = checkContactDates(personalDataDTO);
            if (!errors.isEmpty()) {
                validationErrors.addAll(errors);
            }
        }

        return validationErrors;
    }

    //skjema 02d
    private List<ValidationError> recordDateKnownBornAndMors(final PersonalDataDTO personalDataDTO,
                                                             final LocalDate lowLim,
                                                             final LocalDate maxLim){
        final ArrayList<ValidationError> validationErrors = new ArrayList<>();
        final String deadString = personalDataDTO.getDead();
        final String bornString = personalDataDTO.getBorn();

        if (check(bornString) && check(deadString)) {
            final LocalDate dead = getDate(deadString);
            final LocalDate born = getDate(bornString);

            if (dead.isBefore(lowLim) || dead.isAfter(maxLim)) {
                validationErrors.add(new ValidationError("dod", "UtenforGyldigPeriode"));
            } else {
                if (born.isAfter(dead)) {
                    validationErrors.add(new ValidationError("fodt", "FodtEtterDodt"));
                } else {
                    final List<ValidationError> errorList = checkContactDates(personalDataDTO);
                    if (!errorList.isEmpty()) {
                        validationErrors.addAll(errorList);
                    }
                }
            }
        }

        return validationErrors;
    }

    private List<ValidationError> checkContactDates(final PersonalDataDTO personalDataDTO) {
        final ArrayList<ValidationError> validationErrors = new ArrayList<>();
        final String firstContact = personalDataDTO.getFirstContact();
        final String lastContact = personalDataDTO.getLastContact();
        final String born = personalDataDTO.getBorn();
        final String dead = personalDataDTO.getDead();

        checkFirstAfterLastContact(validationErrors, firstContact, lastContact);
        checkFirstBeforeBorn(validationErrors, firstContact, born);
        checkFirstAfterDead(validationErrors, firstContact, dead);
        checkLastAfterDead(validationErrors, lastContact, dead);
        checkLastBeforeBorn(validationErrors, lastContact, born);
        
        return validationErrors;
    }

    private void checkFirstAfterLastContact(final ArrayList<ValidationError> validationErrors,
                                            final String firstContactString,
                                            final String lastContactString) {
        if (check(firstContactString) && check(lastContactString)) {
            final LocalDate firstContact = getDate(firstContactString);
            final LocalDate lastContact = getDate(lastContactString);
            
            if (firstContact.isAfter(lastContact)) {
                validationErrors.add(new ValidationError("fKontakt", "fKontaktEttersKontakt"));
            }
        }
    }
    
    private void checkFirstBeforeBorn(final ArrayList<ValidationError> validationErrors,
                                      final String firstContactString,
                                      final String bornString) {
        if (check(firstContactString) && check(bornString)) {
            final LocalDate firstContact = getDate(firstContactString);
            final LocalDate born = getDate(bornString);
            
            if (firstContact.isBefore(born)) {
                validationErrors.add(new ValidationError("fKontakt", "fKontaktForFodt"));
            }
        }
    }

    private void checkFirstAfterDead(final ArrayList<ValidationError> validationErrors,
                                     final String firstContactString,
                                     final String deadString) {
        if (check(firstContactString) && check(deadString)) {
            final LocalDate firstContact = getDate(firstContactString);
            final LocalDate dead = getDate(deadString);

            if (firstContact.isAfter(dead)) {
                validationErrors.add(new ValidationError("fKontakt", "fKontaktEtterDod"));
            }
        }
    }

    private void checkLastBeforeBorn(final ArrayList<ValidationError> validationErrors,
                                     final String lastContactString,
                                     final String bornString) {
        if (check(lastContactString) && check(bornString)) {
            final LocalDate born = getDate(bornString);
            final LocalDate lastContact = getDate(lastContactString);
            
            if (born.isAfter(lastContact)) {
                validationErrors.add(new ValidationError("sKontakt", "sKontaktForFodt"));
            }
        }
    }

    private void checkLastAfterDead(final ArrayList<ValidationError> validationErrors,
                                    final String lastContactString,
                                    final String deadString) {
        if (check(lastContactString) && check(deadString)) {
            final LocalDate lastContact = getDate(lastContactString);
            final LocalDate dead = getDate(deadString);

            if (lastContact.isAfter(dead)) {
                validationErrors.add(new ValidationError("sKontakt", "sKontaktEtterDod"));
            }
        }
    }
   
    private LocalDate getDate(final String dato) {
        return ValidDateFormats.getDate(dato);
    }

    private boolean check(final String validationString) {
        if (validationString == null ||
            validationString.isEmpty() ||
            validationString.toLowerCase().equals("mors") ||
            validationString.toLowerCase().equals("m") ||
            validationString.toLowerCase().equals("ukjent") ||
            validationString.toLowerCase().equals("u"))  {

            return false;
        }

        return ValidDateFormats.getDate(validationString) != null;
    }

    private boolean checkMorsUnknown(final String mors, final String other) {
        if (mors == null || mors.isEmpty()) {
            return false;
        }

        return (mors.toLowerCase().equals(other));
    }

}