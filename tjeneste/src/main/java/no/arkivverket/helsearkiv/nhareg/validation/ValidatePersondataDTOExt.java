package no.arkivverket.helsearkiv.nhareg.validation;

import org.apache.commons.lang3.StringUtils;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;

/**
 * Created by haraldk on 20.03.15.
 */
public class ValidatePersondataDTOExt {

    public boolean isValid(PersondataDTO person) {
        try {
           if (StringUtils.isNotEmpty(person.getFodselsnummer()))
               return true;
            if (StringUtils.isNotEmpty(person.getJournalnummer()))
                return true;
            if (StringUtils.isNotEmpty((person.getLopenummer())))
                return true;
            return false;
        }
        catch (final Exception e) {
            System.out.println(e.toString());
        }
        return true;
    }

}
