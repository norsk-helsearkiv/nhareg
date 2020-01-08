package no.arkivverket.helsearkiv.nhareg.validation;

import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;

import java.util.*;

/**
 * Validerer konsistens på datofeltene til PersondataDTO
 * Benyttes i POST og PUt
 * @author robing
 */
public class DateValidation {

    enum DateCompareResult {
        BEFORE,
        EQUAL,
        AFTER
    }

    private final List<String> gyldigMors = Arrays.asList("mors", "m", "M");
    private final List<String> gyldigUkjent = Arrays.asList("ukjent", "u", "U");

    /**
     * Diagnosedato/år valideres med javax.validation
     *
     * @param diagnosis Diagnosis to validate
     * @param medicalRecord Record to validate
     * @return A list of errors found, if any
     */
    public List<ValidationError> validateDiagnosis(final DiagnoseDTO diagnosis, final Pasientjournal medicalRecord) {
        final List<ValidationError> validationErrors = new ArrayList<>();
        final Grunnopplysninger baseProperties = medicalRecord.getGrunnopplysninger();

        //ta diagnosedato som god fisk ettersom både mors og født er ukjent...
        if (baseProperties.getBornDateUnknown() != null &&
            baseProperties.getBornDateUnknown() &&
            baseProperties.getDeathDateUnknown() != null &&
            baseProperties.getDeathDateUnknown()) {

            return validationErrors;
        }

        final String diagnosisDateString = diagnosis.getDiagnosedato();
        final Date diagnosisDate = getDate(diagnosisDateString);

        if (diagnosisDate == null) {
            validationErrors.add(new ValidationError("diagnosedato", "DiagFormatFeil"));
            return validationErrors;
        }

        // fødtdatoår kjent
        if (baseProperties.getBornDateUnknown() == null || !baseProperties.getBornDateUnknown()) {
            final DatoEllerAar born = baseProperties.getBorn();
            final String bornString = DateOrYearConverter.fromDateOrYear(born);

            if ((compareDateString(bornString, diagnosisDateString) == DateCompareResult.AFTER)) {
                validationErrors.add(new ValidationError("diagnosedato", "DiagForFodt"));
            }
        }

        if (baseProperties.getDeathDateUnknown() == null || !baseProperties.getDeathDateUnknown()) {
            final DatoEllerAar dod = baseProperties.getDead();
            final String dodString = DateOrYearConverter.fromDateOrYear(dod);
            if ((compareDateString(dodString, diagnosisDateString) == DateCompareResult.BEFORE)) {
                validationErrors.add(new ValidationError("diagnosedato", "DiagEtterDod"));
            }
        }

        return validationErrors;
    }

    //Hjelpemetoder for validering
    public ArrayList<ValidationError> validate(final PersondataDTO personalDataDTO, final ConfigurationDAO configurationDAO) {
        final ArrayList<ValidationError> validationErrors = new ArrayList<>();

        if (personalDataDTO == null) {
            return validationErrors;
        }

        final String born = personalDataDTO.getFodt();
        final String dead = personalDataDTO.getDod();
        
        //født kan ikke være mors
        if (checkMorsUnknown(born, "mors") &&
            checkMorsUnknown(born, "m")) {
            validationErrors.add(new ValidationError("fodt", "DagEllerAar"));
        }

        //død kan ikke være ukjent
        if (checkMorsUnknown(dead, "ukjent") &&
            checkMorsUnknown(dead, "u")) {
            validationErrors.add(new ValidationError("dod", "DagEllerAar"));
        }

        if (validationErrors.size() > 0) {
            return validationErrors;
        }

        final Date lowLim = configurationDAO.getDate(ConfigurationDAO.KONFIG_LOWLIM);
        final Integer waitLim = configurationDAO.getInt(ConfigurationDAO.KONFIG_WAITLIM);
        final Date maxLim = GyldigeDatoformater.getDateRoll(new Date(), -waitLim);
        final Integer maxAge = configurationDAO.getInt(ConfigurationDAO.KONFIG_MAXAGE);
        final Date minLim = GyldigeDatoformater.getDateRoll(new Date(), -maxAge);

        //skjema 1
        final List<ValidationError> fnumErrors = fnumCheck(personalDataDTO, lowLim, maxLim);
        validationErrors.addAll(fnumErrors);

        //skjema 2a
        if (gyldigUkjent.contains(born)) { //mors sjekkes i metoden pga feilmelding hvis den mangler
            final List<ValidationError> errors = recordDateUnknownFAndMors(personalDataDTO, lowLim, maxLim);
            validationErrors.addAll(errors);
        }

        //skjema 2b
        if (gyldigMors.contains(dead) && check(born)) {
            final List<ValidationError> errors = recordDateKnownFDateUnkownDeath(personalDataDTO, minLim);
            validationErrors.addAll(errors);
        }

        //skjema 2c
        if (gyldigUkjent.contains(born) && check(dead)) {
            final List<ValidationError> errors = recordDateKnownUnkownBornDate(personalDataDTO, lowLim, maxLim);
            validationErrors.addAll(errors);
        }

        //skjema 2d
        if (check(born)&& check(dead)) {
            final List<ValidationError> errors = recordDateKnownBornAndMors(personalDataDTO, lowLim, maxLim);
            validationErrors.addAll(errors);
        }

        return validationErrors;
    }

    //skjema 01
    private ArrayList<ValidationError> fnumCheck(final PersondataDTO person, final Date lowLim, final Date maxLim) {
        final ArrayList<ValidationError> errors = new ArrayList<>();
        final String fnr = person.getFodselsnummer();

        if (!gyldigUkjent.contains(person.getFodt())) {
            if (fnr == null || fnr.isEmpty()) {
                //sjekk fødselsdato
                if (check(person.getFodt())) {
                    final Date born = getDate(person.getFodt());
                    if (born.before(lowLim) || born.after(maxLim)) {
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
    private ArrayList<ValidationError> recordDateUnknownFAndMors(final PersondataDTO personalDataDTO,
                                                                 final Date lowLim,
                                                                 final Date maxLim) {
        final ArrayList<ValidationError> feil = new ArrayList<>();
        final String lastContact = personalDataDTO.getLastContact();
        final String firstContact = personalDataDTO.getFirstContact();
        final String dead = personalDataDTO.getDod();

        if (gyldigMors.contains(dead)) {
            if (check(lastContact) && check(firstContact)) {
                final Date lastContactDate = getDate(lastContact);

                if ((compareDateString(firstContact, lastContact)) == DateCompareResult.AFTER) {
                    feil.add(new ValidationError("fKontakt", "fKontaktEttersKontakt"));
                }

                if (lastContactDate.before(lowLim) || lastContactDate.after(maxLim)) {
                    feil.add(new ValidationError("sKontakt", "UtenforGyldigPeriode"));
                }
            }
        } else if (!check(dead)) {
            feil.add(new ValidationError("dod", "manglermors"));
        }

        return feil;
    }

    //skjema 02b
    private List<ValidationError> recordDateKnownFDateUnkownDeath(final PersondataDTO personalDataDTO, final Date maxAge) {
        final ArrayList<ValidationError> validationErrorList = new ArrayList<>();

        if (check(personalDataDTO.getDod()) || gyldigMors.contains(personalDataDTO.getDod())) {
            final List<ValidationError> validationErrors = checkContactDateBorn(personalDataDTO);
            if (!validationErrors.isEmpty()) {
                validationErrorList.addAll(validationErrors);
            }
        } else {
            if (check(personalDataDTO.getFodt())) {
                final Date born = getDate(personalDataDTO.getFodt());

                if (born.before(maxAge)) {
                    validationErrorList.add(new ValidationError("fodt", "UtenforGyldigPeriode"));
                } else {
                    final List<ValidationError> validationErrors = checkContactDateBorn(personalDataDTO);
                    if (!validationErrors.isEmpty()) {
                        validationErrorList.addAll(validationErrors);
                    }
                }
            }
        }

        return validationErrorList;
    }

    //skjema 02c
    private List<ValidationError> recordDateKnownUnkownBornDate(final PersondataDTO personalDataDTO,
                                                                final Date lowLim,
                                                                final Date maxLim) {
        final ArrayList<ValidationError> validationErrors = new ArrayList<>();
        Date dod = getDate(personalDataDTO.getDod());

        if (dod.before(lowLim) || dod.after(maxLim)) {
            validationErrors.add(new ValidationError("dod", "UtenforGyldigPeriode"));
        } else {
            final List<ValidationError> errors = checkContactDateDead(personalDataDTO);
            if (!errors.isEmpty()){
                validationErrors.addAll(errors);
            }
        }

        return validationErrors;
    }

    //skjema 02d
    private List<ValidationError> recordDateKnownBornAndMors(final PersondataDTO personalDataDTO,
                                                             final Date lowLim,
                                                             final Date maxLim){
        final ArrayList<ValidationError> validationErrors = new ArrayList<>();
        final Date dead = getDate(personalDataDTO.getDod());

        if (dead.before(lowLim) || dead.after(maxLim)) {
            validationErrors.add(new ValidationError("dod", "UtenforGyldigPeriode"));
        } else {
            if (check(personalDataDTO.getFodt()) && check(personalDataDTO.getDod())) {
                if (compareDateString(personalDataDTO.getDod(), personalDataDTO.getFodt()) == DateCompareResult.BEFORE) {
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

    private List<ValidationError> checkContactDates(final PersondataDTO personalDataDTO) {
        final ArrayList<ValidationError> validationErrors = new ArrayList<>();

        //første og siste kontaktdato registrert
        final String firstContact = personalDataDTO.getFirstContact();
        final String lastContact = personalDataDTO.getLastContact();
        final String born = personalDataDTO.getFodt();
        final String dead = personalDataDTO.getDod();

        if (check(firstContact) && check(lastContact)) {
            if (compareDateString(firstContact, lastContact) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("fKontakt", "fKontaktEttersKontakt"));
            }

            //første kan ikke være før født
            if (compareDateString(born, firstContact) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("fKontakt", "fKontaktForFodt"));
            }

            if (compareDateString(lastContact, dead) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("sKontakt", "sKontaktEtterDod"));
            }
        }
        //bare siste kontaktdato registrert
        else if (check(lastContact)) {
            if (compareDateString(born, lastContact) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("sKontakt", "sKontaktForFodt"));
            }

            if (compareDateString(lastContact, dead) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("sKontakt", "sKontaktEtterDod"));
            }
        }
        //bare første kontaktdato registrert
        else if (check(firstContact)) {
            //første kan ikke være før født
            if (compareDateString(born, firstContact) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("fKontakt", "fKontaktForFodt"));
            }

            if (compareDateString(firstContact, dead) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("fKontakt", "fKontaktEtterDod"));
            }
        }

        return validationErrors;
    }

    /**
     * Sjekker kontaktdatoer basert på født
     * @param personalDataDTO data to check
     * @return A list of errors, if any
     */
    private List<ValidationError> checkContactDateBorn(final PersondataDTO personalDataDTO){
        final ArrayList<ValidationError> validationErrors = new ArrayList<>();
        final String firstContact = personalDataDTO.getFirstContact();
        final String lastContact = personalDataDTO.getLastContact();
        final String born = personalDataDTO.getFodt();

        //første og siste kontaktdato registrert
        if (check(firstContact) && check(lastContact)) {
            if (compareDateString(firstContact, lastContact) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("fKontakt", "fKontaktEttersKontakt"));
            }

            //første kan ikke være før født
            if (compareDateString(born, firstContact) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("fKontakt", "fKontaktForFodt"));
            }
        }
        //bare siste kontaktdato registrert
        else if (check(lastContact)) {
            if (compareDateString(born, lastContact) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("sKontakt", "sKontaktForFodt"));
            }

        }
        //bare første kontaktdato registrert
        else if (check(firstContact)) {
            //første kan ikke være før født
            if (compareDateString(born, firstContact) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("fKontakt", "fKontaktForFodt"));
            }
        }

        return validationErrors;
    }

    /**
     * Sjekker kontaktdatoer basert på død
     * @param personalDataDTO data to check
     * @return A list containing errors, if any
     */
    private List<ValidationError> checkContactDateDead(final PersondataDTO personalDataDTO) {
        final ArrayList<ValidationError> validationErrors = new ArrayList<>();
        final String firstContact = personalDataDTO.getFirstContact();
        final String lastContact = personalDataDTO.getLastContact();
        final String dead = personalDataDTO.getDod();
        
        //første og siste kontaktdato registrert
        if (check(firstContact) && check(lastContact)) {
            if (compareDateString(firstContact, lastContact) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("fKontakt", "fKontaktEttersKontakt"));
            }

            //siste kan ikke være etter død
            if (compareDateString(lastContact, dead) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("sKontakt", "sKontaktEtterDod"));
            }
        }
        //bare siste kontaktdato registrert
        else if (check(lastContact)) {
            if (compareDateString(lastContact, dead) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("sKontakt", "sKontaktEtterDod"));
            }

        }
        //bare første kontaktdato registrert
        else if (check(firstContact)) {
            //første kan ikke være etter død
            if (compareDateString(firstContact, dead) == DateCompareResult.AFTER) {
                validationErrors.add(new ValidationError("fKontakt", "fKontaktEtterDod"));
            }
        }
        
        return validationErrors;
    }

    private boolean isOnlyYearPresent(final String date, final String other) {
        return isYearOnly(date) || isYearOnly(other);
    }

    private int getYear(final String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Sammenligner datostrenger, tar høyde for at det kan kun være årstall (da sammenlignes kun år)
     * 
     * @param dateString Date to compare as string
     * @param otherString Date to compare as string
     * @return -1 if the date < other, 0 if date == other, else +1 
     */
    private DateCompareResult compareDateString(final String dateString, final String otherString) {
        if (isOnlyYearPresent(dateString, otherString)) {
            int year = getYear(dateString);
            int otherYear = getYear(otherString);
            
            if (year < otherYear) {
                return DateCompareResult.BEFORE;
            }
            
            if (year == otherYear) {
                return DateCompareResult.EQUAL;
            }
            
            return DateCompareResult.AFTER;
        } else {
            final Date date = getDate(dateString);
            final Date other = getDate(otherString);
            int compare = date.compareTo(other);
            
            if (compare < 0) {
                return DateCompareResult.BEFORE;
            }
            
            if (compare == 0) {
                return DateCompareResult.EQUAL;
            }

            return DateCompareResult.AFTER;
        }
    }

    private Date getDate(final String dato) {
        return GyldigeDatoformater.getDate(dato);
    }

    /**
     * Sjekker om dato kun inneholder 4 siffer
     * @param dato date to check
     * @return true if the date contains only a year, else false.
     */
    private boolean isYearOnly(final String dato) {
        try {
            Integer.parseInt(dato);
            return dato.length() == 4;
        } catch (NumberFormatException e) {
            return false;
        }
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

        return getDate(validationString) != null;
    }

    private boolean checkMorsUnknown(final String mors, final String other) {
        if (mors == null || mors.isEmpty()) {
            return false;
        }

        return (mors.toLowerCase().equals(other));
    }
    
}