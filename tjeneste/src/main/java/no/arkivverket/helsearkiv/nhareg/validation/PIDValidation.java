package no.arkivverket.helsearkiv.nhareg.validation;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

public class PIDValidation {

    /** Length of a valid PID number. */
    private static final int LEN_PID = 11;

    public static ValidationError validate(final String pid) {
        if (pid != null && !pid.isEmpty()) {
            if (validPid(pid)) {
                return null;
            }

            return new ValidationError("fodselsnummer", "FeilFodselsnummer");
        }

        return null;
    }

    public static ValidationError validate(final MedicalRecordDTO medicalRecordDTO) {
        final String fnr = medicalRecordDTO.getPid();
        
        return validate(fnr);
    }

    public static boolean isDNumber(final String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }
        
        final String first = number.substring(0, 1);
        int i = Integer.parseInt(first);

        return i > 3;
    }

    public static boolean isHNumber(final String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }
        
        final String month = number.substring(2, 3);
        int i = Integer.parseInt(month);

        return i > 3;
    }

    public static boolean isFNumber(final String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }
        
        return !isDNumber(number) && !isHNumber(number);
    }
    
    /**
     * Checks the PID formatting.
     *
     * @param  pidString PersonalID number to validate as string
     * @return <code>true</code> if valid format
     */
    public static boolean validPid(final String pidString) {
        if (pidString == null || (pidString.trim().length() != LEN_PID)) {
            return false;
        }
        
        final String pid = pidString.trim();
        int index = 0;
        int partSumOne = 0;
        int partSumTwo = 0;
        final int[] factorOne = new int[] { 3, 7, 6, 1, 8, 9, 4, 5, 2 };
        final int[] factorTwo = new int[] { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };
        
        while (index < factorTwo.length) {
            final int value = Character.digit(pid.charAt(index), 10);
            
            if (index < factorOne.length) { // Digits 1-8
                partSumOne += value * factorOne[index];
                partSumTwo += value * factorTwo[index];
            } else { // Digits 10
                final int mod = 11 - (partSumOne % 11);
                if ( !((mod == value) || (mod == 11 && value == 0))) {
                    return false;
                }
                
                partSumTwo += mod * factorTwo[index];
            }
            
            index++;
        }
        
        final int digitEleven = Character.digit(pid.charAt(index), 10); // digit 11
        final int modEleven = 11 - (partSumTwo % 11);
        
        return ((modEleven == digitEleven) || (modEleven == 11 && digitEleven == 0));
    }
    
}